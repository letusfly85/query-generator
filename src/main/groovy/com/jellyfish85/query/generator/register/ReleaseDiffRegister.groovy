package com.jellyfish85.query.generator.register

import com.jellyfish85.dbaccessor.bean.query.generate.tool.KrReleaseDiffsBean
import com.jellyfish85.dbaccessor.dao.query.generate.tool.KrReleaseDiffsDao
import com.jellyfish85.svnaccessor.manager.SVNManager
import org.apache.commons.lang.ArrayUtils
import org.tmatesoft.svn.core.SVNDirEntry
import org.tmatesoft.svn.core.io.SVNRepository

import java.sql.Connection

class ReleaseDiffRegister {

    private String     tagName       = ""

    private String     envName       = ""

    private KrReleaseDiffsDao  dao   = null

    private KrReleaseDiffsBean currentBean = null

    private KrReleaseDiffsBean nextBean    = null

    private SVNManager manager       = null

    private SVNRepository repository = null

    private Connection    conn       = null

    public ReleaseDiffRegister(Connection _conn, String _tagName, String _envName) {
        this.conn    = _conn
        this.tagName = _tagName
        this.envName = _envName

        this.dao            = new KrReleaseDiffsDao()
        this.currentBean    = new KrReleaseDiffsBean()
        this.nextBean       = new KrReleaseDiffsBean()

        this.manager    = new SVNManager()
        this.repository = manager.getRepository(tagName)
    }

    public void setAttributeFromDB() {
        this.currentBean.tagNameAttr().setValue(this.tagName)

        def _list = dao.find(this.conn, this.currentBean)
        ArrayList<KrReleaseDiffsBean> list = dao.convert(_list)

        if (ArrayUtils.isEmpty(list)) {
            throw new RuntimeException()
        }

        def bean = list.head()

        this.currentBean.tagNameAttr().setValue(this.tagName)
        this.currentBean.headerFlgAttr().setValue(bean.headerFlgAttr().value())
        this.currentBean.fromRevisionAttr().setValue(bean.fromRevisionAttr().value())
        this.currentBean.toRevisionAttr().setValue(bean.toRevisionAttr().value())
        this.currentBean.targetEnvNameAttr().setValue(this.envName)


        this.nextBean.tagNameAttr().setValue(this.tagName)
        this.nextBean.headerFlgAttr().setValue(bean.headerFlgAttr().value())
        this.nextBean.fromRevisionAttr().setValue(bean.fromRevisionAttr().value())
        this.nextBean.toRevisionAttr().setValue(bean.toRevisionAttr().value())
        this.nextBean.targetEnvNameAttr().setValue(this.envName)
    }

    public void setTagName(String _tagName) {
        this.tagName = _tagName
    }

    public void setCurrentBeanTagName(String _tagName) {
        this.currentBean.tagNameAttr().setValue(_tagName)
    }

    public void setSVNAttributeFromSVN() {
        this.nextBean.fromRevisionAttr().setValue(this.currentBean.toRevisionAttr().value())
        this.nextBean.headerFlgAttr().setValue("1")

        this.currentBean.headerFlgAttr().setValue("0")

        SVNDirEntry entry = this.repository.info(".", -1)
        BigDecimal  headRevision = new BigDecimal(entry.getRevision())
        this.nextBean.toRevisionAttr().setValue(headRevision)
    }

    public BigDecimal getLatestRevision() {
        return new BigDecimal(this.repository.getLatestRevision())
    }

    public void register() {
        println("############################################")
        println("#")
        println("# updating KR_RELEASE_DIFFS record")
        println("#")
        println("# tag    is " + this.nextBean.tagNameAttr().value())
        println("# target is " + this.nextBean.targetEnvNameAttr().value())
        println("#")
        println("# from " + this.currentBean.toRevisionAttr().value().toString())
        println("# to   " + this.nextBean.toRevisionAttr().value().toString())
        println("#")
        println("############################################")

        dao.update(conn, this.currentBean)

        dao.insert(conn, this.nextBean)

        conn.commit()
    }
}
