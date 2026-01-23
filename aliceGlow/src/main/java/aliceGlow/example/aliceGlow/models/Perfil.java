package aliceGlow.example.aliceGlow.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Table(name = "perfis")
@Entity
public class Perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String name;

    public Long getId(){return id;}

    public void setId(Long id){this.id = id;}

    public String getName(){return name;}

    public void setName(String name){this.name = name;}
}
