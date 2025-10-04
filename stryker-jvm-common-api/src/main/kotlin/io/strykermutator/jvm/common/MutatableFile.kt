package io.strykermutator.jvm.common

import java.io.File

/**
 * Reference to an original source file and its mutated counterpart.
 */
public interface MutatableFile {

    /**
     * Original source file. This file is never changed in the mutation testing process.
     *
     * The file must point to an existing file that is not a directory.
     */
    public val original: File

    /**
     * Mutated source file. The source file including the mutants is written to this location.
     *
     * The file must point to a file location that is writable and is not a directory.
     */
    public val mutated: File

    /** File extension of the mutatable file. */
    public val extension: String
}

/**
 * Default implementation of a reference to an original source file and its mutated counterpart.
 *
 * Validates that the original file points to an existing file that is not a directory; and the mutated file points to a
 * writable location that is not a directory.
 *
 * @property original the original source file containing the source code to mutate.
 * @property mutated the mutated source file containing or to contain the mutated source code.
 * @constructor Creates a mutatable file with an original and a mutated file reference.
 */
public data class DefaultMutatableFile(override val original: File, override val mutated: File) : MutatableFile {

    /** File extension of the mutatable file. */
    override val extension: String
        get() = original.extension

    init {
        // The original reference must be a file and exist
        require(original.isFile) { "Original file $original is not a file" }
        require(original.exists()) { "Original file $original does not exist" }

        // If the mutated reference exists, it must be a file, and be writable
        if (mutated.exists()) {
            require(mutated.isFile) { "Mutated file $mutated is not a file" }
            require(mutated.canWrite()) { "Mutated file $mutated is not writable" }
        }

        // The file extension of both files must be the same
        require(original.extension == mutated.extension) {
            "Mutated file has a different extension ${mutated.extension} to the original ${original.extension}"
        }
    }
}
