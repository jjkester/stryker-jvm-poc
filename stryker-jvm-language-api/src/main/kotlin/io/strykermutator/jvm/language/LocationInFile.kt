package io.strykermutator.jvm.language

public data class LocationInFile(val line: Int, val column: Int) : Comparable<LocationInFile> {
    init {
        require(line > 0) { "Line must be greater than zero" }
        require(column >= 0) { "Column must be greater than or equal to zero" }
    }

    override fun compareTo(other: LocationInFile): Int {
        return line.compareTo(other.line)
    }
}
