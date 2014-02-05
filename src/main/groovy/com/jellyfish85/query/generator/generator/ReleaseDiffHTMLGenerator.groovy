package com.jellyfish85.query.generator.generator

import com.jellyfish85.dbaccessor.bean.query.generate.tool.KrReleaseDiffsBean
import com.jellyfish85.dbaccessor.bean.src.mainte.tool.VChangesetsBean
import com.jellyfish85.dbaccessor.dao.query.generate.tool.KrReleaseDiffsDao
import com.jellyfish85.dbaccessor.dao.src.mainte.tool.VChangesetsDao
import com.jellyfish85.query.generator.BaseContext
import org.apache.commons.io.FilenameUtils

import java.sql.Connection

class ReleaseDiffHTMLGenerator extends GeneralGenerator {

    private String tagName      = ""

    private Connection conn     = null

    private Connection myConn   = null

    private BaseContext context = null

    public ReleaseDiffHTMLGenerator(
            BaseContext _context, String _tagName,
            Connection _conn,  Connection _myConn
            ) {
        super(_context)
        this.context = super.getBaseContext()

        this.conn    = _conn
        this.myConn  = _myConn
        this.tagName = _tagName
    }

    public void generateHTML() {
        KrReleaseDiffsDao  dao  = new KrReleaseDiffsDao()
        KrReleaseDiffsBean bean = new KrReleaseDiffsBean()

        bean.tagNameAttr().setValue(this.tagName)

        def _list = dao.find(this.conn, bean)
        bean      = dao.convert(_list).head()

        println(bean.fromRevisionAttr().value())
        println(bean.toRevisionAttr().value())
        println(this.context.queryProp.subversionCurrentName())
        VChangesetsDao changeSetsDao = new VChangesetsDao()
        def _vList = changeSetsDao.findByRevisionAndPath(
                this.myConn, bean.fromRevisionAttr().value(),
                bean.toRevisionAttr().value(),
                (this.context.queryProp.subversionCurrentName() + "%"),
                this.context.queryProp.subversionRepositoryCode()
        )

        ArrayList<VChangesetsBean> vList = changeSetsDao.convert(_vList)

        ArrayList<VChangesetsBean> vList_ = new ArrayList<>()
        vList.each {v ->
            if (FilenameUtils.getName(v.pathAttr().value()) != this.context.queryProp.exceptionPg()) {
                vList_.add(v)
            }
        }

        String replaceKeyWord = this.context.queryProp.subversionProjectName()
        vList_.collectAll {VChangesetsBean vBean ->
            vBean.fileNameAttr().setValue(FilenameUtils.getName(vBean.pathAttr().value()))
            vBean.pathAttr().setValue(vBean.pathAttr().value().replaceAll("/${replaceKeyWord}/", ""))
        }

        String hrefHeader = this.context.queryProp.redmineURLRevisionHeader()
        Map map = [
                fromRevision: bean.fromRevisionAttr().value(),
                toRevision:   bean.toRevisionAttr().value(),
                vList:        vList_,
                hrefHeader:   hrefHeader,
        ]

        println(this.context.environment)
        String path = "/com/jellyfish85/query/generator/template/html/${this.context.environment}/releaseDiff.template"

        generate(map, path)
        setPath(this.context.fileNameHelper.requestHTMLReleaseDiffPath())

        writeAppFile()
    }
}
