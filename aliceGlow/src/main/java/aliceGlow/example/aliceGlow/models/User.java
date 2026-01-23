package aliceGlow.example.aliceGlow.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Table(name = "users")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @Email
    @NotBlank
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank
    private String password;

    @ManyToMany
    @JoinTable(name = "user_perfils", joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "perfil_id"))
    private Set<Perfil> perfils = new HashSet<>();

    @Column(nullable = false)
    private LocalDateTime createAt;


    public Long getId(){return id;}

    public void setId(Long id){this.id = id;}

    public String getName(){return name;}

    public void setName(String name){ this.name = name;}

    public String getEmail(){return email;}

    public void setEmail(String email){this.email = email;}

    public String getPassword(){return  password;}

    public void setPassword(String password){ this.password = password;}

    public Set<Perfil> getPerfils(){return perfils;}

    public void setPerfils(Set<Perfil> perfils){ this.perfils = perfils;}

    public LocalDateTime getCreateStump(){return createAt;}

    public void setCreateStump(LocalDateTime createAt){this.createAt = createAt;}
}
