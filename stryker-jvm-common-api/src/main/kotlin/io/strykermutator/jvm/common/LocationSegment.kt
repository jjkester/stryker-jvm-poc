package io.strykermutator.jvm.common

import java.io.File

/**
 * Location in a file referencing a continuous segment.
 *
 * @property file referenced file.
 * @property segment referenced segments in the file.
 */
public data class LocationSegment(val file: File, val segment: ClosedRange<Position>)