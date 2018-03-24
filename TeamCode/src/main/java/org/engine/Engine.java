package org.engine;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.util.Objects;

/**
 * Created by t420 on 9/29/2016.
 * First successful test was 5:00 6 thur oct 2016
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


public abstract class Engine extends OpMode {

    //Array To Hold States
    public State[][] processes = new State[100][100];

    //Array For Holding Threads
    private Thread[] threads = new Thread[100];

    //Array For Holding SubEngins
    private SubEngine[] subEngines = new SubEngine[100];

    //Sub Process States array
    private State[][] subProcesses;

    //Keep Track of processes X and Y
    private int processesX = 0;
    private int processesY = 0;

    //Keep Track of sub Processes X and Y
    private int subX = 0;

    private boolean checkingStates = true;

    boolean isSubEngineinit = false;

    private static String TAG = "PROGRAM.ENGINE: ";
    private static String SUBTAG = "PROGRAM.SUBENGINE";
    private int processIndex = 0;
    private boolean machineFinished = false;
    private boolean opFinished = true;
    private boolean subProcessFinished = true;

    //sets processes
    public void init() {
        //Call Set Processes to fill arrays with states
        setProcesses();

        //Loop through to processes array and initialize states
        for (int i = 0; i < processes.length; i++) {
            for (int y = 0; y < processes.length; y++) {
                if (processes[i][y] != null) {
                    processes[i][y].init();
                    Log.i(TAG, "INIT" + "[" + Integer.toString(i) + "]" + "[" + Integer.toString(y) + "]");
                }
            }
        }

        for (int i = 0; i < subEngines.length; i++){
            if(subEngines[i] != null && subEngines[i].isPreInit()){
                subEngines[i].initStates();
            }
        }

        for(int i=0; i < subEngines.length; i++){
            if(subEngines[i] != null) {
                Log.i(TAG, Integer.toString(i) + Objects.toString(subEngines[i]) + subEngines[i].getName());
            }
        }
    }

    //checks if ops are finished
    public void loop() {

        //check if we are checking states
        if(checkingStates) {
            checkStateFinished();

        //Check if we are checking states inside sub engines
        }else{

            //Run evaluate on sub engines
            if(!subEngines[processIndex].evaluated) {
                Log.i(TAG,"EVALUATING SUB ENGINE : "+Integer.toString(processIndex));
                subEngines[processIndex].evaluate();
                subEngines[processIndex].setEvaluated(true);
                /*Log.i(TAG,"FINISHED EVALUATING SUB ENGINE : "+ subEngines[processIndex].getName()+
                        Integer.toString(processIndex));*/

            }

            //Check if sub engine is runnable
            if(subEngines[processIndex].isRunable()) {

                //check sub engines
                checkSubEngines();
            }else{
                //if engine is not runnable than incrament processIndex and switch to "checking states"
                Log.i(TAG, "SUB ENGINE NOT RUNNABLE : " + "[" + Integer.toString(processIndex) + "]" + "[0]");
                checkingStates = true;
                processIndex++;
            }
        }

    }

    //kills all processes running when program endes
    @Override
    public void stop() {
        //end all states
        for (int x = 0; x < processes.length; x++) {
            for (int y = 0; y < processes.length; y++) {
                if (processes[x][y] != null) {
                    processes[x][y].setFinished(true);
                    processes[x][y].stop();
                    Log.i(TAG, "KILLED OP : " + "[" + Integer.toString(x) + "]" + "[" + Integer.toString(y) + "]");
                } else {
                }
            }
        }

        //End all sub engines
        for(int i = 0; i < subEngines.length; i++ ){
            if(subEngines[i] != null){
                State[][] subStates = subEngines[i].getProcesses();
                for(int x = 0; x < subStates.length; x ++){
                    for(int y = 0; y <subStates.length; y++){
                        if(subStates[x][y] != null){
                            subStates[x][y].setFinished(true);
                            subStates[x][y].stop();
                        }
                    }
                }
            }
        }
        for(int i = 0; i < subEngines.length; i ++){
            if(subEngines[i]!= null){
                subEngines[i].stop();
            }
        }
    }

    public void checkStateFinished(){

        //check to make sure the current state or whole machine isnt finished
        if (!opFinished && !machineFinished) {

            //Loop through to check if all sections of the current
            // state are finished, if so set opFinsished to true
            for (int y = 0; y < processes.length; y++) {

                if (processes[processIndex][y] != null) {
                    if (processes[processIndex][y].getIsFinished()) {
                        opFinished = true;
                        Log.i(TAG, "FINISHED OP : " + "[" + Integer.toString(processIndex) + "]" + "[" + Integer.toString(y) + "]");
                    } else {
                        opFinished = false;
                        break;
                    }
                } else {
                    break;
                }
            }
            if (opFinished) {
                processIndex++;
            }


        } else {
            //If opmode is finished than set up the next set of processes or
            if (processes[processIndex][0] != null) {
                //set next state
                for (int i = 0; i < processes.length; i++) {
                    threads[i] = new Thread(processes[processIndex][i]);
                    threads[i].start();
                }
                opFinished = false;
                Log.i(TAG, "Started State : " + Integer.toString(processIndex));


            }else if(subEngines[processIndex] != null){
                checkingStates = false;
            }
            else if (processes[processIndex][0] == null && !machineFinished) {
                Log.i(TAG, "MACHINE TERMINATED");
                machineFinished = true;
                stop();
            }

        }
    }

    public void checkSubEngines(){


        // Check if sub engines need to be initialized
        if(!isSubEngineinit){
            //Run set Proccesses on the sub engine
            subEngines[processIndex].setProcesses();

            if(!subEngines[processIndex].isPreInit()) {
                subEngines[processIndex].initStates();
            }

            //set subEngineInit to true so this only runs through once
            isSubEngineinit = true;
        }
        if(!subEngines[processIndex].isMachineFinished()){
            //Log.i(TAG,"STARTED CHECKING SUBSTATE PROCESS");
            subEngines[processIndex].checkStates();
            //Log.i(TAG, "FINISEHD CHECKING SUBSTATE PROCESSES");
        }else{
            isSubEngineinit = false;
            Log.i(TAG,"FINISHED SUBENGINE");
            this.processIndex++;
            checkingStates = true;
        }
    }

    //set processes in extended classes
    public abstract void setProcesses();

    public int getProcessIndex() {
        return processIndex;
    }

    //adds the ability to add processes inside states
    public void addInLineProcess(State state,boolean init) {
        for(int i = 0; i < processes.length;i ++){
            if(processes[processIndex][i] == null || processes[processIndex][i].getIsFinished()){
                if(init){
                    state.init();
                }
                processes[processIndex][i] = state;
                Thread thread = new Thread(processes[processIndex][i]);
                thread.start();
                Log.i(TAG, "ADDED THREAD AT : " + "[" + Integer.toString(getProcessIndex()) + "]" + "[" + Integer.toString(i) + "]");
                break;
            }
        }

    }

    //For adding states when setProcesses is called
    public void addState(State state){

        processesY = 0;

        processes[processesX][processesY] = state;

        processesY++;
        processesX++;

        Log.i(TAG, "ADDED NEW STATE AT : " + Integer.toString(processesX) );

    }

    public void addThreadedState(State state){
        processes[processesX -1][processesY] = state;
        processesY++;
    }

    public void addSubEngine(SubEngine subEngine){
        subEngines[processesX] = subEngine;
        processesX++;
    }

}
