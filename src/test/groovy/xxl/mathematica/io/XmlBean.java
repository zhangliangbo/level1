package xxl.mathematica.io;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class XmlBean {

    @XmlElement
    public String name;

    @XmlElement
    public int age;

    @XmlAttribute
    public int state;

    @XmlElement
    public List<XmlBeanChild> goods;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public List<XmlBeanChild> getGoods() {
        return goods;
    }

    public void setGoods(List<XmlBeanChild> goods) {
        this.goods = goods;
    }
}
