package ro.mycode.autovitapi.dto;

import lombok.Data;

@Data
public class MasinaDTO {

    private String marca="";
    private String model="";
    private int an=0;
    private String culoare="";
    public MasinaDTO(String marca, String model, int an, String culoare) {
        this.marca = marca;
        this.model = model;
        this.an = an;
        this.culoare = culoare;
    }
}
