/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto_producti;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author ASUS TUF GAMING
 */
public class managerfactory {

    private EntityManagerFactory emf = null;

    public EntityManagerFactory getentityManagerFactory() {
        return emf = Persistence.createEntityManagerFactory("proyecto_productiPU");
    }

}
