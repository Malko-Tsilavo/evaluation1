package site.easy.to.build.crm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "configuration")
public class Configuration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotNull(message = "Type configuration is required")
    @ManyToOne
    @JoinColumn(name = "id_type_config", nullable = false)
    private TypeConfig typeConfig;

    @NotNull(message = "Key is required")
    @Column(name = "`key`", nullable = false, columnDefinition = "TEXT")
    private String key;

    @NotNull(message = "Value is required")
    @Column(name = "value", nullable = false, columnDefinition = "TEXT")
    private String value;

    // Constructors
    public Configuration() {
    }

    public Configuration(TypeConfig typeConfig, String key, String value) {
        this.typeConfig = typeConfig;
        this.value = value;
        this.key = key;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TypeConfig getTypeConfig() {
        return typeConfig;
    }

    public void setTypeConfig(TypeConfig typeConfig) {
        this.typeConfig = typeConfig;
    }

    public String getKey () {
        return key;
    }

    public void setKey (String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}