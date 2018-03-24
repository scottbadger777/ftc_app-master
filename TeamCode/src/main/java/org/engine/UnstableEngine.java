package org.engine;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.util.ArrayList;

/**
 * Created by t420 on 9/29/2016.
 * First sucess ful test was 5:00 6 thur oct 2016
 */
/*

::::::::: ::::::::::: :::        :::
:+:    :+:    :+:     :+:        :+:
+:+    +:+    +:+     +:+        +:+
+#++:++#+     +#+     +#+        +#+
+#+    +#+    +#+     +#+        +#+
#+#    #+#    #+#     #+#        #+#
######### ########### ########## ##########

 */


public abstract class UnstableEngine extends OpMode {

    //changed robot prefs
    public ArrayList<ArrayList<State>> processes = new ArrayList<>();
    private ArrayList<Thread> threads = new ArrayList<>();
    private int threadX = 0;

    private boolean expandingArray;

    private int stateX = 0;
    private int stateY = 0;

    public volatile double[][][] cache = new double[100][100][100];


    private static String TAG = "PROGRAM.ENGINE: ";
    private int x = 0;
    private int currentProcess = 0;
    private boolean machineFinished = false;
    private boolean opFininished = true;

    private int threadIndex;

    //sets processes
    public void init() {
        setProcesses();
        Log.i(TAG, Integer.toString(processes.size()));
        for (int i = 0; i < processes.size(); i++) {
            for (int y = 0; y < processes.size(); y++) {
                if (processes.get(i).get(y) != null) {
                    processes.get(i).get(y).init();
                    Log.i(TAG, "INIT" + "[" + Integer.toString(i) + "]" + "[" + Integer.toString(y) + "]");
                }
            }
        }


    }

    //checks if ops are finished
    public void loop() {
        if (!opFininished && !machineFinished) {

            for (int y = 0; y < processes.get(x).size()-1; y++) {


                Log.i(TAG,Boolean.toString(processes.get(x).get(y).getIsFinished()));
                if (processes.get(x).get(y).getIsFinished()) {
                    opFininished = true;
                    Log.i(TAG, "FINISHED OP : " + "[" + Integer.toString(x) + "]" + "[" + Integer.toString(y) + "]");
                } else {
                    opFininished = false;
                    break;
                }

            }
            if (opFininished) {
                x++;
            }


        } else {
            if (processes.get(x).get(0) != null) {
                //set next state.
                threadIndex = 0;
                for (int i = 0; i < processes.get(x).size(); i++) {
                    threads.add(i,new Thread(processes.get(x).get(i)));
                    threads.get(i).start();
                    threadIndex ++;
                }
                opFininished = false;
                currentProcess = x;
                Log.i(TAG, "Started State : " + Integer.toString(x));


            } else if (processes.get(x).get(0) == null && !machineFinished) {
                Log.i(TAG, "MACHINE TERMINATED");
                machineFinished = true;
                stop();
            }

        }


    }

    //kills all processes running when program endes
    @Override
    public void stop() {
        for (int x = 0; x < processes.size(); x++) {
            for (int y = 0; y < processes.size(); y++) {
                if (processes.get(x).get(y) != null) {
                    processes.get(x).get(y).setFinished(true);
                    processes.get(x).get(y).stop();
                    Log.i(TAG, "KILLED OP : " + "[" + Integer.toString(x) + "]" + "[" + Integer.toString(y) + "]");
                } else {
                    break;
                }
            }
        }
    }

    //set processes in extended classes
    public abstract void setProcesses();

    public int getProcessIndex() {
        return x;
    }

    //adds the ability to add processes inside states
    public void addInLineProcess(State state,boolean init) {
        for(int i = 0; i < processes.size();i ++){
            if(processes.get(x).get(i) == null || processes.get(x).get(i).getIsFinished()){
                if(init){
                    state.init();
                }
                processes.get(x).add(i,state);
                Thread thread = new Thread(processes.get(x).get(i));
                thread.start();
                Log.i(TAG, "ADDED THREAD AT : " + "[" + Integer.toString(getProcessIndex()) + "]" + "[" + Integer.toString(i) + "]");
                break;
            }
        }

    }

    //Allows other states to end processes on the same index
    private void endProcess(int index, State state) {
        for (int i = 0; i < processes.size(); i++) {
            if (processes.get(index).get(i) == state) {
                processes.get(index).get(i).setFinished(true);
                Log.i(TAG, "FORCED STOP AT : " + "[" + Integer.toString(getProcessIndex()) + "]" + "[" + Integer.toString(i) + "]");
                break;
            }
        }
    }

    public void addState(State state){


        stateY = 0;

        processes.add(new ArrayList<State>());
        processes.get(stateX).add(stateY,state);

        stateY++;
        stateX++;

        Log.i(TAG, "ADDED NEW STATE AT : " + Integer.toString(stateX) );

    }

    public void addStateProcess(State state){
        processes.get(stateX-1).add(stateY,state);
        stateY ++;
    }

    public void addCacheData(int index, int layer, double data) {
        cache[x][index][layer] = data;
    }

    public double getCacheData(int index, int layer) {
        return cache[0][index][layer];
    }


}
