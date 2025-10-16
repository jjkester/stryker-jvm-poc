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
        private var socket: java.net.Socket? = null
        private var port: Int? = null
        private var outputStream: java.io.OutputStream? = null
        init {
            // Ensure socket is closed on JVM shutdown
            Runtime.getRuntime().addShutdownHook(Thread {
                try {
                    outputStream?.close()
                } catch (_: Exception) {}
                try {
                    socket?.close()
                } catch (_: Exception) {}
            })
        }

        @JvmStatic
        public fun mutantActive(id: String): Boolean {
            Thread.currentThread().stackTrace.forEach { println(it) }
            print("mutantActive called with id: $id\n")
            sendCoverage(id)
            return System.getProperty(PREFIX + id) != null
        }

        private fun sendCoverage(id: String) {
            if (port == null) {
                port = System.getProperty("stryker.coverage.port")?.toIntOrNull()
            }
            val p = port ?: return
            try {
                if (socket == null || socket!!.isClosed) {
                    socket = java.net.Socket("localhost", p)
                    outputStream = socket!!.getOutputStream()
                }
                outputStream?.write((id + "\n").toByteArray(Charsets.UTF_8))
                outputStream?.flush()
            } catch (e: Exception) {
                print("Error sending coverage: ${e.message}\n")
                try {
                    outputStream?.close()
                } catch (_: Exception) {}
                try {
                    socket?.close()
                } catch (_: Exception) {}
                socket = null
                outputStream = null
            }
        }

    }


}