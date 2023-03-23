package br.com.compass.capacitacao.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
@Model(adaptables = Resource.class )
public class Client {
    int id;
    String name;

   public Client() {}

    public Client(int id, String name) {
       super();
       this.id = id;
        this.name = name;
    }

    public Client(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Client [id=" + id + ", name=" + name + "]";
    }

}
