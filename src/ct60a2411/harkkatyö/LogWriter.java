package ct60a2411.harkkatyö;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Log writing and reading class
 * 
 * @author Petri Rämö
 * opiskelijanro: 0438578
 * 
 * @author Ilari Sahi
 * opiskelijanro: 0438594
 * 
 * 16.12.2016
 * 
 * This class contains writing and reading to/from log file. This class follows singleton
 * design and that means that writing is initialized once.
 */

public class LogWriter {
    static LogWriter lw = null;
    DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    
    private LogWriter() {
        PrintWriter wr;
        try {
            wr = new PrintWriter("log.txt", "UTF-8");
            Date date = new Date();
            
            // Initialization of log.txt (session starting time)
            wr.write("Istunto aloitettu: " + df.format(date));
            wr.write(System.getProperty("line.separator"));
            wr.write("Lähetetyt tuotteet:");
            wr.write(System.getProperty("line.separator"));
            wr.write(System.getProperty("line.separator"));
            wr.close();
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            Logger.getLogger(LogWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    static public LogWriter getInstance() throws FileNotFoundException, UnsupportedEncodingException {
        if (lw == null) {
            lw = new LogWriter();
        }
        return lw;
    }
    
    /**
     * Writes sent parcel information to log.txt
     * 
     * @param a product name
     * @param b sending SmartPost
     * @param c receiving SmartPost
     * @param d broken status
     * @param e distance travelled
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public void writer(String a, String b, String c, boolean d, String e) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        FileWriter wr = new FileWriter("log.txt", true);
        BufferedWriter bw = new BufferedWriter(wr);
        bw.write("Nimi: ");
        bw.write(a);
        bw.write(System.getProperty("line.separator"));
        bw.write("Lähetyspaikka: ");
        bw.write(b);
        bw.write(System.getProperty("line.separator"));
        bw.write("Saapumispaikka: ");
        bw.write(c);
        bw.write(System.getProperty("line.separator"));
        Date date = new Date();
        bw.write("Lähetysaika: ");
        bw.write(df.format(date));
        bw.write(System.getProperty("line.separator"));
        bw.write("Kuljetettu matka: ");
        bw.write(e + " km");
        bw.write(System.getProperty("line.separator"));
        if (d) {
            bw.write("Tuote meni rikki kuljetuksen aikana");
        } else {
            bw.write("Tuote ei mennyt rikki kuljetuksen aikana");
        }
        bw.write(System.getProperty("line.separator"));
        bw.write(System.getProperty("line.separator"));
        bw.close();
        
    }
    
    /**
     * 
     * @return log.txt contents
     * @throws FileNotFoundException
     * @throws IOException
     */
    public String reader() throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader("log.txt"));
        String sb = "";
        String line = br.readLine();
        while(line != null) {
            sb += line + "\n";
            line = br.readLine();
            
        }
        br.close();
        return sb;
    }
    
    /**
     * 
     * These last three functions are used when you close the program and it 
     * writes to log file amount of packages you have send, total distance and what
     * has left at warehouse.
     */
    
    public void endWrite(String a, String b) throws IOException {
        FileWriter wr = new FileWriter("log.txt", true);
        BufferedWriter bw = new BufferedWriter(wr);
        bw.write("Lähetettyjen pakettien määrä: ");
        bw.write(a);
        bw.write(System.getProperty("line.separator"));
        bw.write("Lähetettyjen pakettien kilometrit yhteensä: ");
        bw.write(b);
        bw.write(System.getProperty("line.separator"));
        bw.write(System.getProperty("line.separator"));
        bw.close();
    }
    
    public void endInitWarehouse() throws IOException {
        FileWriter wr = new FileWriter("log.txt", true);
        BufferedWriter bw = new BufferedWriter(wr);
        bw.write(System.getProperty("line.separator"));
        bw.write("Warehouse:");
        bw.write(System.getProperty("line.separator"));
        bw.write(System.getProperty("line.separator"));
        bw.close();

    }
    
    public void endWriteWarehouse(String a, String b, String c, String d) throws IOException {
        FileWriter wr = new FileWriter("log.txt", true);
        BufferedWriter bw = new BufferedWriter(wr);
        bw.write("Nimi: ");
        bw.write(a);
        bw.write(System.getProperty("line.separator"));
        bw.write("Lähetyspaikka: ");
        bw.write(b);
        bw.write(System.getProperty("line.separator"));
        bw.write("Saapumispaikka: ");
        bw.write(c);
        bw.write(System.getProperty("line.separator"));
        bw.write("Kuljetettava matka: ");
        bw.write(d + " km");
        bw.write(System.getProperty("line.separator"));
        bw.write(System.getProperty("line.separator"));
        bw.close();
    }
    
}
