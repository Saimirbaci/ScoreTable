/**
 * Made by saimirbaci on 20/10/14.
 */
package com.saimirbaci.PesKatshTabel;

public class Records{
    private int value;
    private boolean value_set;
    private boolean automaticValueSet;

    public boolean isAutomaticValueSet() {
        return automaticValueSet;
    }

    public void setAutomaticValueSet(boolean automaticValueSet) {
        this.automaticValueSet = automaticValueSet;
    }

    public void init(){
        this.value = 0;
        this.value_set = false;
        this.automaticValueSet = false;
    }

    public void setValue(int value){
        this.value = value;
    }

    public void setValue_set(){
        this.value_set = true;
    }

    public void unsetValue_set(){
        this.value_set = false;
    }

    public int getValue(){
        return value;
    }

    public boolean isValue_set(){
        return value_set;
    }
}