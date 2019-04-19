package org.o7planning.sbshoppingcart.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.o7planning.sbshoppingcart.entity.*;
import org.o7planning.sbshoppingcart.form.LoaiBanhForm;
import org.o7planning.sbshoppingcart.model.*;
import org.o7planning.sbshoppingcart.pagination.PaginationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.util.List;

@Transactional
@Repository
public class LoaiBanhDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public LoaiBanh findLoaiBanh(String code) {
        try {
            String sql = "Select e from " + LoaiBanh.class.getName() + " e Where e.code =:code ";

            Session session = this.sessionFactory.getCurrentSession();
            Query<LoaiBanh> query = session.createQuery(sql, LoaiBanh.class);
            query.setParameter("code", code);
            return (LoaiBanh) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public LoaiBanh findLoaiBanhName(String name) {
        try {
            String sql = "Select e from " + LoaiBanh.class.getName() + " e Where e.name =:name ";

            Session session = this.sessionFactory.getCurrentSession();
            Query<LoaiBanh> query = session.createQuery(sql, LoaiBanh.class);
            query.setParameter("name", name);
            return (LoaiBanh) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    public LoaiBanhInfo findLoaiInfo(String code) {
        LoaiBanh loaiBanh = this.findLoaiBanh(code);
        if (loaiBanh == null) {
            return null;
        }
        return new LoaiBanhInfo(loaiBanh.getCode(), loaiBanh.getName());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void save(LoaiBanhForm loaiBanhForm) {

        Session session = this.sessionFactory.getCurrentSession();
        String code = loaiBanhForm.getCode();

        LoaiBanh loaiBanh = null;

        boolean isNew = false;
        if (code != null) {
            loaiBanh = this.findLoaiBanh(code);
        }
        if (loaiBanh == null) {
            isNew = true;
            loaiBanh = new LoaiBanh();
        }
        loaiBanh.setCode(code);
        loaiBanh.setName(loaiBanhForm.getName());
        if (loaiBanhForm.getFileData() != null) {
            byte[] image = null;
            try {
                image = loaiBanhForm.getFileData().getBytes();
            } catch (IOException e) {
            }
            if (image != null && image.length > 0) {
                loaiBanh.setImage(image);
            }
        }
        if (isNew) {
            session.persist(loaiBanh);
        }
        // If error in DB, Exceptions will be thrown out immediately
        session.flush();
    }

    public PaginationResult<LoaiBanhInfo> queryLoaiBanhs(int page, int maxResult, int maxNavigationPage,
                                                        String likeName) {
        String sql = "Select new " + LoaiBanhInfo.class.getName() //
                + "(p.code, p.name) " + " from "//
                + LoaiBanh.class.getName() + " p ";
        if (likeName != null && likeName.length() > 0) {
            sql += " Where lower(p.name) like :likeName ";
        }
        //
        Session session = this.sessionFactory.getCurrentSession();
        Query<LoaiBanhInfo> query = session.createQuery(sql, LoaiBanhInfo.class);

        if (likeName != null && likeName.length() > 0) {
            query.setParameter("likeName", "%" + likeName.toLowerCase() + "%");
        }
        return new PaginationResult<LoaiBanhInfo>(query, page, maxResult, maxNavigationPage);
    }

    public PaginationResult<LoaiBanhInfo> queryProducts(int page, int maxResult, int maxNavigationPage) {
        return queryLoaiBanhs(page, maxResult, maxNavigationPage, null);
    }

    public List<BanhInfo> listBanhInfos(String loaiBanhId) {
        String sql = "Select new " + BanhInfo.class.getName() //
                + "(d.code, d.name, d.price)"//
                + " from " + Banh.class.getName() + " d ";//
        if (loaiBanhId != null && loaiBanhId.length() > 0) {
           sql += "where lower(d.loaiBanh.code) like :loaiBanhId ";
        }
        sql += " order by d.code desc ";
        Session session = this.sessionFactory.getCurrentSession();
        Query<BanhInfo> query = session.createQuery(sql, BanhInfo.class);
        if (loaiBanhId != null && loaiBanhId.length() > 0) {
            query.setParameter("loaiBanhId", loaiBanhId);
        }

        return query.getResultList();
    }
}

