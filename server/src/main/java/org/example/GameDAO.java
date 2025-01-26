package org.example;

import entities.GameInfo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class GameDAO{
  private SessionFactory sessions;

  public void setSessions(SessionFactory sessionFactory) {
    this.sessions = sessionFactory;
  }


  public void saveGame(GameInfo game) {
    Session session = sessions.openSession();
    session.beginTransaction();
    session.save(game);
    session.getTransaction().commit();
    session.close();
  }

  public GameInfo loadGame(long id){
    Session session = sessions.openSession();
    GameInfo gi = session.get(GameInfo.class, id);
    session.close();
    return gi;
  }
}
