package io.strykermutator.jvm.common

/**
 * Position in text based on a line number (1-indexed) and column (0-indexed).
 *
 * @property line line number, starting with 0.
 * @property column column number, starting with 1.
 * @constructor Creates a new location referencing the provided line and column.
 */
public data class Position(val line: Int, val column: Int) : Comparable<Position> {
    init {
        require(line > 0) { "Line must be greater than zero" }
        require(column >= 0) { "Column must be greater than or equal to zero" }
    }

    override fun compareTo(other: Position): Int {
        return line.compareTo(other.line).takeIf { it != 0 } ?: column.compareTo(other.column)
    }
}
