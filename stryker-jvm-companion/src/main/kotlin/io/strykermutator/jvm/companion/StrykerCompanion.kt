package io.strykermutator.jvm.companion

public class StrykerCompanion {

    public companion object {
        private const val PREFIX = "mutant_"

        @JvmStatic
        public fun mutantActive(id: String): Boolean {
            return System.getProperty(PREFIX + id) != null
        }

    }


}