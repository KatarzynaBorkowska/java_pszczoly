package com.example.projekt_kb;


import javafx.animation.PathTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;
import java.util.Random;
import java.util.concurrent.Semaphore;
import static java.lang.System.currentTimeMillis;

public class Ul extends Thread{

    private int wejscie;
    int animacja = 100;

    private int  n,gdzie,umieranie,s,time,od,do_czas,rodzi_czas,umiera_po;
    public long czas = currentTimeMillis();
    private static char[] znaki = {'+', '-','/','@','%'};
    private int[] pszczoly;
    private volatile Semaphore semaphore;
    public Ul( int n, Semaphore semaphore,int[] pszczoly,int gdzie,int umieranie,int s,int time,int wejscie,int od, int do_czas,int rodzi_czas,int umiera_po) {
        this.n = n;
        this.semaphore = semaphore;
        this.pszczoly = new int[]{,};
        this.pszczoly = pszczoly;
        this.gdzie = gdzie;
        this.umieranie = umieranie;
        this.s = s;
        this.time = time;
        this.wejscie = wejscie;
        this.od = od;
        this.do_czas = do_czas;
        this.rodzi_czas = rodzi_czas;
        this.umiera_po = umiera_po;
    }

    public void run() {
        try {
            dzialanieSynchr();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void dzialanieSynchr() throws InterruptedException {
        Random random = new Random();

        for(int i = 0; ; i++) {
            try {
                Thread.sleep(random.nextInt(10) + 1);
            } catch (InterruptedException e) {}
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {}



            if((currentTimeMillis() - czas) > rodzi_czas && (pszczoly[0] + pszczoly[1]) < s){


                if(gdzie == 1 && umieranie == 0 && pszczoly[0] < s/2 )
                {

                    rodzi();
                    porod();
                    semaphore.release();
                    sleep(random.nextInt(do_czas - od + 1) + od);
                    semaphore.acquire();
                    pierwszy_raz_opuszcza_ul();
                    if(wejscie == 0)
                    {
                        wylot_1();
                    }else if(wejscie == 1){
                        wylot_2();
                    }
                }


            }
            else if(gdzie == 1 && pszczoly[1] > 0 && pszczoly[0] < s/2 && time == 0)
            {
                wchodzi_do_ul();

                if(wejscie == 0)
                {
                    wlatywanie1();
                }else{
                    wlatywanie2();
                }

                semaphore.release();
                sleep(random.nextInt(do_czas - od + 1) + od);
                semaphore.acquire();
                wychodzi_z_ul();
                if(umieranie == umiera_po)
                {
                    umiera();
                    umieranie();
                }
                else{
                    wychodzi();
                    if(wejscie == 0)
                    {
                        wylot_1();
                    }else{
                        wylot_2();
                    }
                }
            }

            semaphore.release();
        }
    }

    public void rodzi(){
        pszczoly[0]++;
        gdzie = 0; // w ulu
        umieranie++;
        time = 0;
        System.out.println("Krolowa rodzi Pszczoly w ulu " + pszczoly[0] + ", pszczoly poza ulem " + pszczoly[1]);
    }

    public void pierwszy_raz_opuszcza_ul(){
        pszczoly[0]--;
        gdzie = 1; // poza ulem
        pszczoly[1]++;
        czas = currentTimeMillis();
        System.out.println("Pszczola wychodzi pierwszy raz z ula, Pszczoly w ulu drzwiami nr " + (wejscie + 1) + " Pszczoly w ulu " + pszczoly[0] + ", pszczoly poza ulem " + pszczoly[1] );
    }

    public void wchodzi_do_ul(){
        pszczoly[0]++; // pszczoły w ulu
        pszczoly[1]--;
        gdzie = 0; // w ulu
        umieranie++; // liczy za ile umrze
        time = 0;
        System.out.println("Pszczola wchodzi drzwiami nr " + (wejscie + 1) + " Pszczoly w ulu " + pszczoly[0] + ", pszczoly poza ulem " + pszczoly[1]);
    }

    public void wychodzi_z_ul(){
        pszczoly[0]--;
        gdzie = 1; // poza ulem
    }

    public void wychodzi(){
        pszczoly[1]++;
        gdzie = 1;
        System.out.println("Pszczola wychodzi drzwiami nr " + (wejscie + 1) + " Pszczoly w ulu " + pszczoly[0] + ", pszczoly poza ulem " + pszczoly[1]);
    }

    public void umiera(){
        umieranie = 0;
        gdzie = 1;
        System.out.println("Pszczola umiera, Pszczoly w ulu " + pszczoly[0] + ", pszczoly poza ulem " + pszczoly[1]);
    }

    public void wylot_2(){
        Circle circle = new Circle(320, 220, 10);

        circle.setFill(Color.GREENYELLOW);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(2);

        Platform.runLater(() -> {
            Application.anchor.getChildren().add(circle);
        });


        ScaleTransition scaleTransition = new ScaleTransition();
        scaleTransition.setDuration(Duration.millis(animacja));
        scaleTransition.setNode(circle);

        scaleTransition.setOnFinished(e -> {
            synchronized (this) {
                notify();
            }
        });


        scaleTransition.play();


        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Path path = new Path();
        MoveTo moveTo = new MoveTo();
        moveTo.setX(circle.getCenterX());
        moveTo.setY(circle.getCenterY());
        LineTo lineTo = new LineTo();
        lineTo.setX(110);
        lineTo.setY(220);
        path.getElements().addAll(moveTo, lineTo);
        PathTransition pathTransition = new PathTransition(Duration.millis(2*animacja), path, circle);
        pathTransition.setOnFinished(e -> {
            synchronized (this) {
                notify();
            }
        });

        pathTransition.play();
        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Platform.runLater(() -> {
            Application.anchor.getChildren().remove(circle);
        });
    }

    public void wylot_1(){
            Circle circle = new Circle(320, 190, 10);

            circle.setFill(Color.YELLOWGREEN);
            circle.setStroke(Color.BLACK);
            circle.setStrokeWidth(2);

            Platform.runLater(() -> {
                Application.anchor.getChildren().add(circle);
            });


            ScaleTransition scaleTransition = new ScaleTransition();
            scaleTransition.setDuration(Duration.millis(animacja));
            scaleTransition.setNode(circle);

            scaleTransition.setOnFinished(e -> {
                synchronized (this) {
                    notify();
                }
            });


            scaleTransition.play();


            synchronized (this) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            Path path = new Path();
            MoveTo moveTo = new MoveTo();
            moveTo.setX(circle.getCenterX());
            moveTo.setY(circle.getCenterY());
            LineTo lineTo = new LineTo();
            lineTo.setX(110);
            lineTo.setY(190);
            path.getElements().addAll(moveTo, lineTo);
            PathTransition pathTransition = new PathTransition(Duration.millis(2*animacja), path, circle);
            pathTransition.setOnFinished(e -> {
                synchronized (this) {
                    notify();
                }
            });

            pathTransition.play();
            synchronized (this) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Platform.runLater(() -> {
                Application.anchor.getChildren().remove(circle);
            });
        }


    public void wlatywanie1(){
        Circle circle = new Circle(100, 190, 10);

        circle.setFill(Color.YELLOW);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(2);

        Platform.runLater(() -> {
            Application.anchor.getChildren().add(circle);
        });


        ScaleTransition scaleTransition = new ScaleTransition();
        scaleTransition.setDuration(Duration.millis(animacja));
        scaleTransition.setNode(circle);

        scaleTransition.setOnFinished(e -> {
            synchronized (this) {
                notify();
            }
        });


        scaleTransition.play();


        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Path path = new Path();
        MoveTo moveTo = new MoveTo();
        moveTo.setX(circle.getCenterX());
        moveTo.setY(circle.getCenterY());
        LineTo lineTo = new LineTo();
        lineTo.setX(320);
        lineTo.setY(190);
        path.getElements().addAll(moveTo, lineTo);
        PathTransition pathTransition = new PathTransition(Duration.millis(2*animacja), path, circle);
        pathTransition.setOnFinished(e -> {
            synchronized (this) {
                notify();
            }
        });

        pathTransition.play();
        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Platform.runLater(() -> {
            Application.anchor.getChildren().remove(circle);
        });
    }

    public void wlatywanie2(){
        Circle circle = new Circle(110, 220, 10);

        circle.setFill(Paint.valueOf("#E5E619"));
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(2);

        Platform.runLater(() -> {
            Application.anchor.getChildren().add(circle);
        });


        ScaleTransition scaleTransition = new ScaleTransition();
        scaleTransition.setDuration(Duration.millis(animacja));
        scaleTransition.setNode(circle);

        scaleTransition.setOnFinished(e -> {
            synchronized (this) {
                notify();
            }
        });


        scaleTransition.play();


        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Path path = new Path();
        MoveTo moveTo = new MoveTo();
        moveTo.setX(circle.getCenterX());
        moveTo.setY(circle.getCenterY());
        LineTo lineTo = new LineTo();
        lineTo.setX(320);
        lineTo.setY(220);
        path.getElements().addAll(moveTo, lineTo);
        PathTransition pathTransition = new PathTransition(Duration.millis(2*animacja), path, circle);
        pathTransition.setOnFinished(e -> {
            synchronized (this) {
                notify();
            }
        });

        pathTransition.play();
        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Platform.runLater(() -> {
            Application.anchor.getChildren().remove(circle);
        });
    }

    public void umieranie(){
        Circle circle = new Circle(320, 205, 10);

        circle.setFill(Color.BLACK);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(2);

        Platform.runLater(() -> {
            Application.anchor.getChildren().add(circle);
        });


        ScaleTransition scaleTransition = new ScaleTransition();
        scaleTransition.setDuration(Duration.millis(animacja));
        scaleTransition.setNode(circle);

        scaleTransition.setOnFinished(e -> {
            synchronized (this) {
                notify();
            }
        });


        scaleTransition.play();


        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Path path = new Path();
        MoveTo moveTo = new MoveTo();
        moveTo.setX(circle.getCenterX());
        moveTo.setY(circle.getCenterY());
        LineTo lineTo = new LineTo();
        lineTo.setX(0);
        lineTo.setY(0);
        path.getElements().addAll(moveTo, lineTo);
        PathTransition pathTransition = new PathTransition(Duration.millis(2*animacja), path, circle);
        pathTransition.setOnFinished(e -> {
            synchronized (this) {
                notify();
            }
        });

        pathTransition.play();
        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Platform.runLater(() -> {
            Application.anchor.getChildren().remove(circle);
        });
    }

    public void porod(){
        Circle circle = new Circle(500, 150, 10);

        circle.setFill(Color.WHITE);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(2);

        Platform.runLater(() -> {
            Application.anchor.getChildren().add(circle);
        });


        ScaleTransition scaleTransition = new ScaleTransition();
        scaleTransition.setDuration(Duration.millis(animacja));
        scaleTransition.setNode(circle);

        scaleTransition.setOnFinished(e -> {
            synchronized (this) {
                notify();
            }
        });


        scaleTransition.play();


        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Path path = new Path();
        MoveTo moveTo = new MoveTo();
        moveTo.setX(circle.getCenterX());
        moveTo.setY(circle.getCenterY());
        LineTo lineTo = new LineTo();
        lineTo.setX(355);
        lineTo.setY(150);
        path.getElements().addAll(moveTo, lineTo);
        PathTransition pathTransition = new PathTransition(Duration.millis(2*animacja), path, circle);
        pathTransition.setOnFinished(e -> {
            synchronized (this) {
                notify();
            }
        });

        pathTransition.play();
        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Platform.runLater(() -> {
            Application.anchor.getChildren().remove(circle);
        });
    }

}
