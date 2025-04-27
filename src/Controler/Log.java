package Controler;

import java.io.*;

public class Log {

        private static PrintStream fileStream;
        private static PrintStream originalStream;

        // Appelle cette méthode au tout début du programme
        public static void startLogging(String fileName) throws FileNotFoundException {
            // Sauvegarder l'original
            originalStream = System.out;
            // Créer un nouveau PrintStream pour écrire dans le fichier
            fileStream = new PrintStream(new FileOutputStream(fileName, true)); // true = append mode
            // Rediriger System.out
            System.setOut(new PrintStream(new MultiOutputStream(originalStream, fileStream)));
        }

        // Appelle cette méthode à la fin du programme pour bien fermer les flux
        public static void stopLogging() {
            if (fileStream != null) {
                fileStream.close();
            }
            if (originalStream != null) {
                System.setOut(originalStream); // Restaurer la console normale
            }
        }

        // Stream personnalisé pour écrire à la fois dans la console ET dans le fichier
        private static class MultiOutputStream extends OutputStream {
            private final OutputStream[] streams;

            public MultiOutputStream(OutputStream... streams) {
                this.streams = streams;
            }

            @Override
            public void write(int b) throws IOException {
                for (OutputStream s : streams) {
                    s.write(b);
                }
            }

            @Override
            public void flush() throws IOException {
                for (OutputStream s : streams) {
                    s.flush();
                }
            }

            @Override
            public void close() throws IOException {
                for (OutputStream s : streams) {
                    s.close();
                }
            }
        }
}
