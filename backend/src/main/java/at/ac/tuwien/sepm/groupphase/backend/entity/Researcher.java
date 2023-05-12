package at.ac.tuwien.sepm.groupphase.backend.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "researcher")
public class Researcher extends ApplicationUser {

}
