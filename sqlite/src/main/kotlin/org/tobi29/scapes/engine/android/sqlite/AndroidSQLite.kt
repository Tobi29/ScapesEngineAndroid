/*
 * Copyright 2012-2018 Tobi29
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tobi29.scapes.engine.android.sqlite

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import org.tobi29.io.use
import org.tobi29.sql.*

class AndroidSQLite(private val connection: SQLiteDatabase) : SQLDatabase {
    override fun createTable(
        name: String,
        primaryKey: Array<out String>,
        columns: Array<out SQLColumn>
    ) {
        val sql = StringBuilder(512)
        sql.append("CREATE TABLE IF NOT EXISTS ").append(name).append(" (")
        var first = true
        for (column in columns) {
            if (first) {
                first = false
            } else {
                sql.append(',')
            }
            sql.append(column.name).append(' ')
            sql.append(sqlType(column.type, column.extra))
            if (column.notNull) {
                sql.append(" NOT NULL")
            }
            if (column.unique) {
                sql.append(" UNIQUE")
            }
        }
        sql.append(", PRIMARY KEY (")
        first = true
        for (column in primaryKey) {
            if (first) {
                first = false
            } else {
                sql.append(',')
            }
            sql.append(column)
        }
        sql.append(')')
        for (column in columns) {
            val foreignKey = column.foreignKey
            if (foreignKey != null) {
                sql.append(", FOREIGN KEY (").append(column.name).append(')')
                sql.append(" REFERENCES ").append(foreignKey.table)
                sql.append('(').append(foreignKey.column).append(')')
                sql.append(" ON UPDATE ").append(foreignKey.onUpdate.sql)
                sql.append(" ON DELETE ").append(foreignKey.onDelete.sql)
            }
        }
        sql.append(");")
        val compiled = sql.toString()
        val statement = connection.compileStatement(compiled)
        statement.execute()
    }

    override fun dropTable(name: String) {
        val compiled = "DROP TABLE IF EXISTS $name;"
        val statement = connection.compileStatement(compiled)
        statement.execute()
    }

    override fun compileQuery(
        table: String,
        columns: Array<out String>,
        matches: Array<out SQLMatch>
    ): SQLQuery {
        val where = StringBuilder(columns.size shl 5)
        sqlWhere(matches, where)
        val compiledWhere = where.toString()
        return { values ->
            val argsWhere = arrayOfNulls<String>(values.size)
            for ((i, value) in values.withIndex()) {
                if (value is ByteArray) {
                    throw IllegalArgumentException(
                        "Byte array value not supported"
                    )
                }
                argsWhere[i] = value.toString()
            }
            val result = ArrayList<Array<Any?>>()
            connection.query(
                table, columns, compiledWhere, argsWhere, null,
                null, null
            ).use { cursor ->
                while (cursor.moveToNext()) {
                    result.add(resolveResult(cursor))
                }
            }
            result
        }
    }

    override fun compileInsert(
        table: String,
        columns: Array<out String>
    ): SQLInsert {
        val columnsSafe = columns.clone()
        return { values ->
            for (row in values) {
                if (row.size != columnsSafe.size) {
                    throw IllegalArgumentException(
                        "Amount of updated values (${row.size}) does not match amount of columns (${columnsSafe.size})"
                    )
                }
                val content = ContentValues()
                for (i in columnsSafe.indices) {
                    resolveObject(row[i], columnsSafe[i], content)
                }
                connection.insertWithOnConflict(
                    table, null, content,
                    SQLiteDatabase.CONFLICT_IGNORE
                )
            }
        }
    }

    override fun compileUpdate(
        table: String,
        matches: Array<out SQLMatch>,
        columns: Array<out String>
    ): SQLUpdate {
        val columnsSafe = columns.clone()
        val where = StringBuilder(64)
        sqlWhere(matches, where)
        val compiledWhere = where.toString()
        return { values, updates ->
            if (updates.size != columnsSafe.size) {
                throw IllegalArgumentException(
                    "Amount of updated values (${updates.size}) does not match amount of columns (${columnsSafe.size})"
                )
            }
            val argsWhere = arrayOfNulls<String>(matches.size)
            for ((i, match) in values.withIndex()) {
                if (match is ByteArray) {
                    throw IllegalArgumentException(
                        "Byte array value not supported"
                    )
                }
                argsWhere[i] = match.toString()
            }
            val content = ContentValues()
            for (i in columns.indices) {
                resolveObject(updates[i], columns[i], content)
            }
            connection.update(table, content, compiledWhere, argsWhere)
        }
    }

    override fun compileReplace(
        table: String,
        columns: Array<out String>
    ): SQLReplace {
        val columnsSize = columns.size
        return { values ->
            for (row in values) {
                if (row.size != columnsSize) {
                    throw IllegalArgumentException(
                        "Amount of updated values (${row.size}) does not match amount of columns ($columnsSize)"
                    )
                }
                val content = ContentValues()
                for (i in columns.indices) {
                    resolveObject(row[i], columns[i], content)
                }
                connection.replace(table, null, content)
            }
        }
    }

    override fun compileDelete(
        table: String,
        matches: Array<out SQLMatch>
    ): SQLDelete {
        val where = StringBuilder(64)
        sqlWhere(matches, where)
        val compiledWhere = where.toString()
        return { values ->
            val argsWhere = arrayOfNulls<String>(matches.size)
            for ((i, match) in values.withIndex()) {
                if (match is ByteArray) {
                    throw IllegalArgumentException(
                        "Byte array value not supported"
                    )
                }
                argsWhere[i] = match.toString()
            }
            connection.delete(table, compiledWhere, argsWhere)
        }
    }

    fun dispose() {
        connection.close()
    }

    private fun resolveObject(
        value: Any?,
        column: String,
        content: ContentValues
    ) {
        when (value) {
            is Byte -> content.put(column, value)
            is Short -> content.put(column, value)
            is Int -> content.put(column, value)
            is Long -> content.put(column, value)
            is Float -> content.put(column, value)
            is Double -> content.put(column, value)
            is ByteArray -> content.put(column, value)
            is String -> content.put(column, value)
            null -> content.putNull(column)
        }
    }

    private fun resolveResult(cursor: Cursor): Array<Any?> {
        val columns = cursor.columnCount
        val row = arrayOfNulls<Any>(columns)
        for (i in 0 until columns) {
            when (cursor.getType(i)) {
                Cursor.FIELD_TYPE_NULL -> row[i] = null
                Cursor.FIELD_TYPE_INTEGER -> row[i] = cursor.getLong(i)
                Cursor.FIELD_TYPE_FLOAT -> row[i] = cursor.getDouble(i)
                Cursor.FIELD_TYPE_BLOB -> row[i] = cursor.getBlob(i)
                Cursor.FIELD_TYPE_STRING -> row[i] = cursor.getString(i)
            }
        }
        return row
    }
}
