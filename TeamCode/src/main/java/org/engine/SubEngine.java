package org.engine;

import android.util.Log;

/**
 * Created by goldfishpi on 12/2/17.
 */

public abstract class SubEngine {

    private State[][] processes = new State[100][100];
    private Thread[] threads = new Thread[100];

    private SubEngine[] subEngines = new SubEngine[100];
    int stateX = 0;
    int stateY = 0;

    int currentX = 0;
    int currentY = 0;

    private boolean checkingStates = true;

    private boolean machineFinished = false;
    private boolean opFininished = true;

    private boolean isInitalized = false;

    private boolean runable = false;

    private boolean preInit= false;

    public static String TAG = "PROGRAM.SUBENGINE.CLEAN";

    boolean evaluated = false;

    public abstract void setProcesses();

    public abstract void evaluate();

    private String name = "";


    public void checkStates(){

        //check to make sure the current state or whole machine isnt finished
        if (!opFininished && !machineFinished) {

            //Loop through to check if all sections of the current
            // state are finished, if so set opFinsished to true
            for (int y = 0; y < processes.length; y++) {

                if (processes[currentX][y] != null) {
                    if (processes[currentX][y].getIsFinished()) {
                        opFininished = true;
                        Log.i(TAG, "FINISHED OP : " + "[" + Integer.toString(currentX) + "]" + "[" + Integer.toString(y) + "]");
                    } else {
                        opFininished = false;
                        break;
                    }
                } else {
                    break;
                }
            }
            if (opFininished) {
                currentX++;
            }


        } else {
            //If opmode is finished than set up the next set of processes or
            if (processes[currentX][0] != null) {
                //set next state
                for (int i = 0; i < processes.length; i++) {
                    if (processes[currentX][i] == null) { break; }
                    threads[i] = new Thread(processes[currentX][i]);
                    threads[i].start();
                }
                opFininished = false;
                Log.i(TAG, "Started State : " + Integer.toString(currentX));


            }else if (processes[currentX][0] == null && !machineFinished) {
                Log.i(TAG, "MACHINE TERMINATED");
                machineFinished = true;
                stop();
            }else{

            }

        }
    }

    public void initStates(){
        for(int i = 0; i < processes.length; i ++){
            for(int y = 0; y < processes.length; y ++ ){
                if(processes[i][y] != null){
                    String msg ="INTITALIZED SUBSTATE : " +"[" + Integer.toString(i) +"]" + "["+Integer.toString(y)+"]";
                    Log.i(TAG,msg );
                    processes[i][y].init();
                }
            }
        }
    }

    public void stop(){
        for(int x = 0; x < processes.length; x++){
            for (int y = 0; y < processes.length; y++){
                if(processes[x][y] != null){
                    processes[x][y].stop();
                    Log.i(TAG,"KILLED OP AT : ["+x+"]"+"["+y+"]");
                }
            }

        }
    }

    public void addState(State state){
        stateY = 0;
        processes[stateX][stateY] = state;
        Log.i(TAG, "CREATED NEW STATE : " + "["+stateX + "]" + "[" + stateY +"]" );
        stateX ++;
    }

    public void addThreadedState(State state){
        stateY ++;
        processes[stateX-1][stateY] = state;
        Log.i(TAG, "ADDED PROCCES TO STATE : " + "["+stateX + "]" + "[" + stateY +"]" );
    }

    public boolean isPreInit() {
        return preInit;
    }

    public void setPreInit(boolean preInit) {
        this.preInit = preInit;
    }

    public int getCurrentX() {
        return currentX;
    }

    public int getCurrentY() {
        return currentY;
    }

    public State[][] getProcesses() {
        return processes;
    }

    public boolean isRunable() {
        return runable;
    }

    public boolean isMachineFinished() {
        return machineFinished;
    }

    public void setRunable(boolean runable) {
        this.runable = runable;
    }

    public boolean isEvaluated() {
        return evaluated;
    }

    public void setEvaluated(boolean evaluated) {
        this.evaluated = evaluated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public boolean isInitalized() {
        return isInitalized;
    }

    public void setInitalized(boolean initalized) {
        isInitalized = initalized;
    }
}
