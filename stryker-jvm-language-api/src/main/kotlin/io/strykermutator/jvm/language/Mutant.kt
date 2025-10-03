package io.strykermutator.jvm.language

public interface Mutant {
    public val id: String
    public val location: ClosedRange<LocationInFile>
    public val name: String
    public val replacement: String
    public val guardedReplacement: String
}