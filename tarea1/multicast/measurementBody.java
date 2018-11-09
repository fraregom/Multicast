package project.multicast;

import java.io.Serializable;

public class measurementBody implements Serializable{

    private Integer id;
    private String variable;
    private Integer value;

    public measurementBody(int id, String variable, int value) {
        this.id = id;
        this.variable = variable;
        this.value = value;
    }

    public Integer getId() {
        return id;
    }

    public String getVariable() {
        return variable;
    }

    public Integer getValue() {
        return value;
    }

}
