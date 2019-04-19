package org.o7planning.sbshoppingcart.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.o7planning.sbshoppingcart.entity.*;
import org.o7planning.sbshoppingcart.form.*;
import org.o7planning.sbshoppingcart.model.*;
import org.o7planning.sbshoppingcart.pagination.PaginationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@Transactional
@Repository
public class BanhDAO {

    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private LoaiBanhDAO loaiBanhDAO;


    public Banh findBanh(String code) {
        try {
            String sql = "Select e from " + Banh.class.getName() + " e Where lower(e.code) like:code ";

            Session session = this.sessionFactory.getCurrentSession();
            Query<Banh> query = session.createQuery(sql, Banh.class);
            if (code != null && code.length() > 0) {
                query.setParameter("code", "%" + code.toLowerCase() + "%");
            }
            return (Banh) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public BanhInfo findBanhInfo(String code) {
        Banh banh = this.findBanh(code);
        if (banh == null) {
            return null;
        }
        return new BanhInfo(banh.getCode(), banh.getName(), banh.getPrice());
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void insert(Banh banh)
    {
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(banh);
        session.flush();
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void update(Banh banh)
    {
        Session session = this.sessionFactory.getCurrentSession();
        session.update(banh);
        session.flush();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void save(BanhForm banhForm) {

        Session session = this.sessionFactory.getCurrentSession();
        String code = banhForm.getCode();
        LoaiBanh loaiBanh = null;
        loaiBanh = this.loaiBanhDAO.findLoaiBanh(banhForm.getLoaiBanh());
        Banh banh = null;

        boolean isNew = false;

        if (code != null) {
            banh = this.findBanh(code);
        }
        if (banh == null) {
            isNew = true;
            banh = new Banh();
        }
        banh.setCode(code);
        banh.setName(banhForm.getName());
        banh.setPrice(banhForm.getPrice());
        banh.setLoaiBanh(loaiBanh);
        if (banhForm.getFileData() != null) {
            byte[] image = null;
            try {
                image = banhForm.getFileData().getBytes();
            } catch (IOException e) {
            }
            if (image != null && image.length > 0) {
                banh.setImage(image);
            }
        }
        if (isNew) {
            session.persist(banh);
        }
        // If error in DB, Exceptions will be thrown out immediately
        session.flush();
    }
    public List<BanhInfo> timBanh(String likeName)
    {
        String sql = "Select new " + BanhInfo.class.getName() //
                + "(p.code, p.name, p.price) " + " from "//
                + Banh.class.getName() + " p ";
        if (likeName != null && likeName.length() > 0) {
            sql += " Where lower(p.name) like :likeName ";
        }
        sql += " order by p.code desc ";
        //
        Session session = this.sessionFactory.getCurrentSession();
        Query<BanhInfo> query = session.createQuery(sql, BanhInfo.class);

        if (likeName != null && likeName.length() > 0) {
            query.setParameter("likeName", "%" + likeName.toLowerCase() + "%");
        }
        return query.getResultList();
    }

    // List danh sach banh
    public PaginationResult<BanhInfo> queryBanhs(int page, int maxResult, int maxNavigationPage,
                                                       String likeName) {
        String sql = "Select new " + BanhInfo.class.getName() //
                + "(p.code, p.name, p.price) " + " from "//
                + Banh.class.getName() + " p ";
        if (likeName != null && likeName.length() > 0) {
            sql += " Where lower(p.name) like :likeName ";
        }
        sql += " order by p.code desc ";
        //
        Session session = this.sessionFactory.getCurrentSession();
        Query<BanhInfo> query = session.createQuery(sql, BanhInfo.class);

        if (likeName != null && likeName.length() > 0) {
            query.setParameter("likeName", "%" + likeName.toLowerCase() + "%");
        }
        return new PaginationResult<BanhInfo>(query, page, maxResult, maxNavigationPage);
    }

    public PaginationResult<BanhInfo> queryBanhs(int page, int maxResult, int maxNavigationPage) {
        return queryBanhs(page, maxResult, maxNavigationPage, null);
    }

}
