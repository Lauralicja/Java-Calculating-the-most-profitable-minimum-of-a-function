package boitzo;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.Scanner;
import static javax.imageio.ImageIO.getCacheDirectory;

public class Boitzo {

    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static void FuncWithY(LinkedList<Double> argFirst, LinkedList<Double> argSec, LinkedList<Double> p, LinkedList<LinkedList> point) {

        for (int i = 0; i < argFirst.size(); i++) {

            LinkedList<Double> temp = new LinkedList();

            double ay;
            double ax = 0;
            if (argSec.get(i) != 0) {

                ay = round((p.get(i) / argSec.get(i)), 4);

                temp.add(ax);
                temp.add(ay);

                int counter = 0;
                for (int k = 0; k < argFirst.size(); k++) {
                    double help = argFirst.get(k) * ax + argSec.get(k) * ay;
                    double pComp = p.get(k);

                    if (help <= pComp && ax >= 0 && ay >= 0 && !point.contains(temp)) {
                        counter++;
                    }

                }
                if (counter == 4) {
                    point.add(temp);
                }
            }
        }
    }

    public static void FuncWithX(LinkedList<Double> argFirst, LinkedList<Double> argSec, LinkedList<Double> p, LinkedList<LinkedList> point) {

        for (int i = 0; i < argFirst.size(); i++) {

            LinkedList<Double> temp = new LinkedList();
            double ax;
            if (argFirst.get(i) != 0) {
                ax = round((p.get(i)) / (argFirst.get(i)), 4);

                double ay = 0;

                temp.add(ax);
                temp.add(ay);

                int counter = 0;
                for (int k = 0; k < argFirst.size(); k++) {
                    double help = argFirst.get(k) * ax + argSec.get(k) * ay;
                    double pComp = p.get(k);

                    if (help <= pComp && ax >= 0 && ay >= 0 && !point.contains(temp)) {
                        counter++;
                    }

                }
                if (counter == 4) {
                    point.add(temp);
                }
            }
        }
    }

    public static void FuncWithFunc(LinkedList<Double> argFirst, LinkedList<Double> argSec, LinkedList<Double> p, LinkedList<LinkedList> point) {
        for (int i = 0; i < argFirst.size(); i++) {
            for (int j = 0; j < argSec.size(); j++) {
                LinkedList<Double> temp = new LinkedList();
                if (j != i) {
                    double ax;
                    double ay;

                    if (round((argFirst.get(i) * argSec.get(j) - argFirst.get(j) * argSec.get(i)), 4) != 0) {

                        ax = round((p.get(i) * argSec.get(j) - p.get(j) * argSec.get(i)) / (argFirst.get(i) * argSec.get(j) - argFirst.get(j) * argSec.get(i)), 4);
                        ay = round((p.get(j) * argFirst.get(i) - p.get(i) * argFirst.get(j)) / (argFirst.get(i) * argSec.get(j) - argFirst.get(j) * argSec.get(i)), 4);

                        temp.add(ax);
                        temp.add(ay);

                        int counter = 0;
                        for (int k = 0; k < argFirst.size(); k++) {
                            double help = argFirst.get(k) * ax + argSec.get(k) * ay;
                            double pComp = p.get(k);

                            if (help <= pComp && ax >= 0 && ay >= 0 && !point.contains(temp)) {
                                counter++;
                            }

                        }
                        if (counter == 4) {
                            point.add(temp);
                        }
                    }

                }
            }
        }
    }

    public static void SolveDual(LinkedList<Double> argFirst, LinkedList<Double> argSec, LinkedList<Double> c, LinkedList x, LinkedList<Double> p, LinkedList<LinkedList> point) { // 1. dla PD

        FuncWithFunc(argFirst, argSec, p, point);
        FuncWithX(argFirst, argSec, p, point);
        FuncWithY(argFirst, argSec, p, point);

        System.out.println("- " + point); // PUNKTY OGRANICZAJACE
        Min(point, x, c);
    }

    public static void Min(LinkedList<LinkedList> point, LinkedList x, LinkedList<Double> c) {
        double max = 0;
        for (int i = 0; i < point.size(); i++) {
            LinkedList<Double> temp;
            temp = point.get(i);
            double result = temp.get(0) * c.get(0) + temp.get(1) * c.get(1);    // przeciecia z osiami
            if (result > max) {
                max = result;
                if (x.size() > 0) {
                    x.clear();
                }
                x.add(temp.get(0));
                x.add(temp.get(1));

            }
        }
        System.out.println("- Wartość minimalna: " + max);
    }

    // WYZNACZNIKI -> x = Pn*Bm - Pm*Bn / An*Bm - Am*Bn   y = An*Pm - Am*Pn / An*Bm - Am*Bn
    public static void Checking(LinkedList<Double> argFirst, LinkedList<Double> argSec, LinkedList<Double> x, LinkedList<Double> p, LinkedList<Double> c, LinkedList<LinkedList> point) {
        LinkedList<Integer> temp = new LinkedList();
        LinkedList<Double> results = new LinkedList();
        for (int i = 0; i < argFirst.size(); i++) {
            double tmp = round((argFirst.get(i) * x.get(0) + argSec.get(i) * x.get(1)), 4);
            if (tmp != p.get(i)) {
                argFirst.set(i, 0.0);
                argSec.set(i, 0.0);

            } else {
                temp.add(i);
            }
        }

        double ax;
        double ay;

        if (temp.size() == 1) {
            System.out.println("- Nieskończenie wiele rozwiązań.");
        }

        if (temp.size() == 2) {
            if (round(argFirst.get(temp.get(0)) * argSec.get(temp.get(1)) - argFirst.get(temp.get(1)) * argSec.get(temp.get(0)), 4) == 0) {
                ax = 0;
                ay = 0;
            } else {
                ax = round((c.get(0) * argSec.get(temp.get(1)) - c.get(1) * argFirst.get(temp.get(1))) / (argFirst.get(temp.get(0)) * argSec.get(temp.get(1)) - argFirst.get(temp.get(1)) * argSec.get(temp.get(0))), 4);
                ay = round((c.get(1) * argFirst.get(temp.get(0)) - c.get(0) * argSec.get(temp.get(0))) / (argFirst.get(temp.get(0)) * argSec.get(temp.get(1)) - argFirst.get(temp.get(1)) * argSec.get(temp.get(0))), 4);
            }
            for (int i = 0; i < argFirst.size(); i++) {
                if (i == temp.get(0)) {
                    results.add(ax);
                } else if (i == temp.get(1)) {
                    results.add(ay);
                } else {
                    results.add(0.0);
                }
            }
            System.out.println("- Punkt W = (" + results + ")");
        }

    }

    public static void addToArr(LinkedList arg, LinkedList c, Scanner scan) {
        LinkedList<Double> argRow = new LinkedList<>();
        String cos = (scan.nextLine()).replaceAll("x\\d|\\s|G\\(|,|\\)|->|\\=|>=|<=|min", "");   // odczytane z pliku, string, format: znakliczba1 znakliczba2...

        String[] splits = cos.split("[*]+"); // rozdzielenie
        int sizetemp = splits.length; // zapisanie kazdego

        for (int j = 0; j < sizetemp; j++) {

            String temp = splits[j];
            double foo = Double.parseDouble(temp); // zmiana na double ze string
            argRow.add(foo);    // dodanie do listy 
        }

        if (scan.hasNextLine()) {   // jesli ma nastepna linie -> wczytujemy ostatnia wartosc do c
            int temp = argRow.size() - 1;
            c.add(argRow.get(temp));
            argRow.remove(temp);
            for (int j = 0; j < argRow.size(); j++) {
                arg.add(argRow.get(j));
            }

        } else {   // jesli nie ma nastepnej linii -> wczytujemy calosc do p
            for (int j = 0; j < argRow.size(); j++) {
                arg.add(argRow.get(j));
            }
        }

    }

    public static void CheckLines(Scanner scan, int lines) {
        while (scan.hasNextLine()) {
            lines++;
            scan.nextLine();
        }
        if (lines < 3) {
            System.out.println("Za mało linii w pliku.");
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        File file;
        try {
            file = new File(getCacheDirectory(), "plik.txt");
            Scanner scan = new Scanner(file);

            LinkedList<LinkedList> point = new LinkedList();   // (y1,y2)
            LinkedList<Double> argFirst = new LinkedList(); // argumenty z pierwszego warunku
            LinkedList<Double> argSec = new LinkedList(); // argumenty z drugiego warunku
            LinkedList<Double> c = new LinkedList(); // prawa strona rownan
            LinkedList<Double> x = new LinkedList();
            LinkedList<Double> p = new LinkedList(); // G(x) = p1x1 +/- p2x2 +/- ...  +/-pnxn
            int lines = 0;

            CheckLines(scan, lines);
            scan = new Scanner(file);

            addToArr(argFirst, c, scan); // dodajemy pierwszy warunek
            addToArr(argSec, c, scan);  // dodajemy drugi warunek
            addToArr(p, c, scan);          // dodajemy p
            SolveDual(argFirst, argSec, c, x, p, point);
            Checking(argFirst, argSec, x, p, c, point);

        } catch (NullPointerException e) {
            System.out.println("Brak pliku.");
        }

    }
}
