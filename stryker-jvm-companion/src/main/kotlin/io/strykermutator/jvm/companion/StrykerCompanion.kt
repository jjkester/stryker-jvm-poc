package io.strykermutator.jvm.companion

/**
 * Companion class to Stryker mutation testing.
 *
 * A companion class must be available on the classpath during mutation testing. The class and method can be configured.
 * The method must be static and have a single argument of type String.
 */
public class StrykerCompanion {

    public companion object {
        private const val PREFIX = "mutant_"

        @JvmStatic
        public fun mutantActive(id: String): Boolean {
            return System.getProperty(PREFIX + id) != null
        }
    }
}