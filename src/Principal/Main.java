package Principal;

import EfficientConvertisseurXml.XMLOutputTarget;
import Validation.Validateur;
import com.snowtide.PDF;
import com.snowtide.pdf.*;
import com.snowtide.pdf.annot.Annotation;
import com.snowtide.pdf.forms.Form;
import com.snowtide.pdf.layout.Image;
import com.sun.xml.internal.ws.commons.xmlutil.Converter;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {

    /**
     * A main method suitable for using this class' functionality from the command line.
     * All of the command-line arguments will be taken to be paths to input PDF documents;
     * each PDF documents will be opened by {@link PDF}, and its content piped
     * through a <code>XMLOutputTarget</code> instance.  Each PDF's extracted content
     * is then written to a ".xml" file in the same directory as the input
     * document.
     *
     * @deprecated Command-line usage of this class may be moved or removed in future PDFxStream releases.
     */

    public static void main(String[] args) throws Exception, IOException {

        //Instanciation de la classe Validateur
        Validateur test = new Validateur(args);
        File input = new File(args[0]);
        File output = new File("./fichierPDFA2.pdf");

        //Test si le fichier d'entrée est valide PDFA
        if (!test.validation()) {

            FileInputStream fileInputStream = null;
            FileOutputStream fileOutputStream = null;


            try {
                fileInputStream = new FileInputStream(input);
                fileOutputStream = new FileOutputStream(output);

                System.out.println("\n*******  TEST DE LA VALIDITÉ DU DOCUMENT **********");
                Runtime runtime = Runtime.getRuntime();
                System.out.println("\n*******  CONVERSION PDF VERS PDFA  **********\n\nTraitement de la conversion PDF vers PDFA...");

                runtime.exec("gs -dPDFA -dBATCH -dNOPAUSE -dUseCIEColor -sProcessColorModel=DeviceCMYK -sDEVICE=pdfwrite -sPDFACompatibilityPolicy=1 -sOutputFile=" + output + " " + input + "");

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                fileInputStream.close();
                fileOutputStream.close();
            }

            File sortieXML = new File(output.getAbsolutePath() + ".xml");

            FileInputStream outFileInputStream = null;
            FileOutputStream sortieFileOutputStream = null;


            try {
                outFileInputStream = new FileInputStream(output);
                sortieFileOutputStream = new FileOutputStream(sortieXML);

                for (int i = 0; i < args.length; i++) {
                    File src = new File(args[i]);
                    if (!src.exists()) {
                        System.out.println("Fichier introuvable: " + args[i]);
                    }
                    if (!src.canRead()) {
                        System.out.println("Impossible de lire le fichier: " + args[i]);
                    }

                    Document stream = PDF.open(src);
                    XMLOutputTarget target = new XMLOutputTarget();
                    stream.pipe(target);
                    OutputStreamWriter writer = new OutputStreamWriter(sortieFileOutputStream, "UTF-8");
                    writer.write(target.getXMLAsString());
                    writer.flush();
                    writer.close();
                    stream.close();

                }
            } catch (IOException e) {

                e.printStackTrace();
            } finally {
                outFileInputStream.close();
                sortieFileOutputStream.close();
            }
        } else
            System.out.println("Felicitation");
    }
}