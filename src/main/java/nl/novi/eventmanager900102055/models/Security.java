package nl.novi.eventmanager900102055.models;

import jakarta.persistence.*;

import java.util.ArrayList;

@Entity
@Table(name = "securities")

public class Security {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    private ArrayList<String>()role =new ArrayList<>();
//    private ArrayList<String>()permissions =new ArrayList<>();
}
