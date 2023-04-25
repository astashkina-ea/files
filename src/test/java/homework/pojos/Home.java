package homework.pojos;

import java.util.List;

public class Home {

    private List<DevicesItem> devices;
    private String name;
    private Integer id;

    public List<DevicesItem> getDevices(){
        return devices;
    }

    public String getName(){
        return name;
    }

    public int getId(){
        return id;
    }
}
