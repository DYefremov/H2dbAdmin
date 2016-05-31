package by.post.data;

/**
 * @author Dmitriy V.Yefremov
 */
public class Column implements Data {

  private String name;
  private String type;
  private Object data;

  public Column(){

  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Object getData() {
    return data;
  }

  public void setData(Object data) {
    this.data = data;
  }

  @Override
  public String toString() {
    return "Column{" +
        "name='" + name + '\'' +
        ", type='" + type + '\'' +
        ", data=" + data +
        '}';
  }
}
