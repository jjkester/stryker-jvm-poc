package io.strykermutator.jvm.common

import java.io.File

/**
 * Location in a file referencing a single position.
 *
 * @property file referenced file.
 * @property position referenced position in the file.
 */
public data class Location(val file: File, val position: Position)