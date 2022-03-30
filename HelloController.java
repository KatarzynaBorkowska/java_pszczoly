package com.example.projekt_kb;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class HelloController {

    static int[] pszczoly;
    static int gdzie;
    static int umieranie;
    static int time;
    int liczba_p,i,od_cz,do_cz,rodzi_co, wejscie1,wejscie2,umiera_po;
    private String s,s1,s2,s3,s4;
    private int a = 10,b = 100,c = 500 ,d = 1000,e = 5;

    @FXML
    private Button start, zmiany,zapisz_zmiany,zapiszMenu;


    @FXML
    private TextField ile_pszczol, od_czas, do_czas,rodzi, umiera;

    @FXML
    private Label welcomeText;
    
    @FXML
    protected void bez_zmian(){
        liczba_p = a;
        od_cz = b;
        do_cz = c;
        rodzi_co = d;
        umiera_po = e;
        start.setDisable(false);
        zmiany.setDisable(true);
        zapisz_zmiany.setDisable(true);
    }

    @FXML
    protected void zapisz() {
        if (!Objects.equals(s, "") && s != null)
            liczba_p = Integer.parseInt(s);
        else
            liczba_p = a;
        if (!Objects.equals(s1, "") && s1!= null)
            od_cz = Integer.parseInt(s1);
        else
            od_cz = b;
        if (!Objects.equals(s2, "") && s2!= null)
            do_cz = Integer.parseInt(s2);
        else
            do_cz = c;
        if (!Objects.equals(s3, "") && s3!= null)
            rodzi_co = Integer.parseInt(s3);
        else
            rodzi_co = d;
        if (!Objects.equals(s4, "") && s4!= null)
            umiera_po = Integer.parseInt(s4);
        else
            umiera_po = e;
        zmiany.setDisable(true);
        zapisz_zmiany.setDisable(true);
        zapiszMenu.setDisable(true);

    }
    @FXML
    public void L_pszczol()
    {

        for (i = 0; i < ile_pszczol.getText().length(); i++)
            if (ile_pszczol.getText().charAt(i) < '0' || ile_pszczol.getText().charAt(i) > '9') {
                ile_pszczol.setText("");
            }
        s = ile_pszczol.getText();
        zmiany.setDisable(true);
        
    }


    @FXML
    public void od_czas(){
        for (i = 0; i < od_czas.getText().length(); i++)
            if (od_czas.getText().charAt(i) < '0' || od_czas.getText().charAt(i) > '9') {
                od_czas.setText("");
            }
        s1 = od_czas.getText();
        zmiany.setDisable(true);
    }



    @FXML
    public void do_czas(){
        for (i = 0; i < do_czas.getText().length(); i++)
            if (do_czas.getText().charAt(i) < '0' || do_czas.getText().charAt(i) > '9') {
                do_czas.setText("");
            }
        s2 = do_czas.getText();
        zmiany.setDisable(true);
    }

    @FXML
    public void RodziCo()

    {
        for (i = 0; i < rodzi.getText().length(); i++)
            if (rodzi.getText().charAt(i) < '0' || rodzi.getText().charAt(i) > '9') {
                rodzi.setText("");
            }
        s3 = rodzi.getText();
        zmiany.setDisable(true);
    }

    @FXML
    public void UmieraCo()
    {
        for (i = 0; i < umiera.getText().length(); i++)
            if (umiera.getText().charAt(i) < '0' || umiera.getText().charAt(i) > '9') {
                umiera.setText("");
            }
        s4 = umiera.getText();
        zmiany.setDisable(true);
    }


    @FXML
    protected void onHelloButtonClick() throws FileNotFoundException {

        zapiszMenu.setOnAction(e ->{
            File plik = new File("dane.txt");
            Scanner wczytajDane = null;
            try {
                wczytajDane = new Scanner(plik);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            String dane = wczytajDane.nextLine();
            ile_pszczol.setText(dane);
            dane = wczytajDane.nextLine();
            od_czas.setText(dane);
            dane = wczytajDane.nextLine();
            do_czas.setText(dane);
            dane = wczytajDane.nextLine();
            rodzi.setText(dane);
            dane = wczytajDane.nextLine();
            umiera.setText(dane);
            start.setDisable(true);
            wczytajDane.close();
            liczba_p = Integer.parseInt(ile_pszczol.getText());
            od_cz = Integer.parseInt(od_czas.getText());
            do_cz = Integer.parseInt(do_czas.getText());
            rodzi_co = Integer.parseInt(rodzi.getText());
            umiera_po = Integer.parseInt(umiera.getText());
            start.setDisable(false);
            zapiszMenu.setDisable(true);
            PrintWriter zapiszDane = null;
            try{
                zapiszDane = new PrintWriter("dane.txt");
            }catch (FileNotFoundException e1){
                e1.printStackTrace();
            }
            zapiszDane.println(liczba_p);
            zapiszDane.println(od_cz);
            zapiszDane.println(do_cz);
            zapiszDane.println(rodzi_co);
            zapiszDane.println(umiera_po);
            zapiszDane.close();
        });

        welcomeText.setText("Witaj w ulu!");
        Semaphore semaphore = new Semaphore(1);
        pszczoly = new int[]{0, liczba_p};
        gdzie = 1;
        umieranie = 0;
        time = 0;
        wejscie1 = 0;
        wejscie2 = 1;
        Thread[] sem = new Thread[liczba_p];
        Thread[] sem2 = new Thread[liczba_p];
        for(int i = 0; i< liczba_p; i++) {
            sem[i] = new Ul( 20, semaphore,pszczoly,gdzie,umieranie, liczba_p,time,wejscie1,od_cz, do_cz,rodzi_co,umiera_po);
            sem2[i] = new Ul( 20, semaphore,pszczoly,gdzie,umieranie, liczba_p,time,wejscie2, od_cz, do_cz,rodzi_co,umiera_po);
        }
        for(int i = 0; i< liczba_p; i++) {
            sem[i].start();
        }
        for(int i = 0; i< liczba_p; i++) {
            sem2[i].start();
        }
        start.setDisable(true);
        zmiany.setDisable(true);
        zapisz_zmiany.setDisable(true);

    }
}