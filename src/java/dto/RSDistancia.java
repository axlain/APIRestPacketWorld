package dto;

public class RSDistancia {
    private String cp1;
    private String cp2;
    private double distancia;

    public RSDistancia() {
    }

    public RSDistancia(String cp1, String cp2, double distancia) {
        this.cp1 = cp1;
        this.cp2 = cp2;
        this.distancia = distancia;
    }

    public String getCp1() {
        return cp1;
    }

    public String getCp2() {
        return cp2;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setCp1(String cp1) {
        this.cp1 = cp1;
    }

    public void setCp2(String cp2) {
        this.cp2 = cp2;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }
}
