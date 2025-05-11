package Controler;

import java.io.*;

/**
 * La classe Log permet de rediriger les sorties de la console vers un fichier tout en conservant l'affichage dans la console.
 */
public class Log {

        private static PrintStream fileStream;
        private static PrintStream originalStream;


    /**
      * Appelle cette méthode au tout début du programme pour commencer la journalisation.
      *
      * @param fileName Le nom du fichier dans lequel écrire les logs.
      * @throws FileNotFoundException Si le fichier ne peut pas être créé ou ouvert.
      */


    // Appelle cette méthode au tout début du programme
        public static void startLogging(String fileName) throws FileNotFoundException {
            // Sauvegarder l'original
            originalStream = System.out;
            // Créer un nouveau PrintStream pour écrire dans le fichier
            fileStream = new PrintStream(new FileOutputStream(fileName, true)); // true = append mode
            // Rediriger System.out
            System.setOut(new PrintStream(new MultiOutputStream(originalStream, fileStream)));
        }


    /**
      * Appelle cette méthode à la fin du programme pour bien fermer les flux.
      */


    // Appelle cette méthode à la fin du programme pour bien fermer les flux
        public static void stopLogging() {
            if (fileStream != null) {
                fileStream.close();
            }
            if (originalStream != null) {
                System.setOut(originalStream); // Restaurer la console normale
            }
        }


    /**
      * Stream personnalisé pour écrire à la fois dans la console ET dans le fichier.
      */


    // Stream personnalisé pour écrire à la fois dans la console ET dans le fichier
        private static class MultiOutputStream extends OutputStream {
            private final OutputStream[] streams;


        /**
          * Constructeur de MultiOutputStream.
          *
          * @param streams Les flux de sortie vers lesquels écrire.
          */


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
