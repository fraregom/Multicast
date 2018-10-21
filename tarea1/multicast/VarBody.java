package tarea1.multicast;

import java.io.Serializable;

public class VarBody implements Serializable{

    private Integer id;
    private String variable;
    private Integer value;

    public VarBody(int id, String variable, int value) {
        this.id = id;
        this.variable = variable;
        this.value = value;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
