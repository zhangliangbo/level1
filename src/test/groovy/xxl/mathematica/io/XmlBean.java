package xxl.mathematica.io;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "XmlBean")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class XmlBean {

    @XmlElement(name = "Name")
    public String name;

    @XmlElement(name = "Age")
    public int age;

    @XmlAttribute(name = "name")
    public int state;

    @XmlElement(name = "Goods")
    public List<String> goods;

    @Override
    public String toString() {
        return "XmlBean{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", state=" + state +
                ", goods=" + goods +
                '}';
    }
}
