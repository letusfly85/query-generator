package com.jellyfish85.query.generator.generator

import org.apache.commons.io.FileUtils

/**
 * == GeneralGenerator ==
 *
 * @author wada shunsuke
 * @since  2013/12/02
 *
 */
class GeneralGenerator {

    private String query = null

    private String path  = null

    /**
     * == initializeQuery ==
     *
     * @author wada shunsuke
     * @since  2013/12/03
     *
     */
    protected void initializeQuery() {
        this.query = null
    }

    public void setQuery(String _query) {
        this.query = _query
    }

    public String getQuery() {
        return this.query
    }

    public void setPath(String _path) {
        this.path = _path
    }

    public String getPath() {
        return this.path
    }

    /**
     * == writeAppFile ==
     *
     * @author wada shunsuke
     * @since  2013/12/06
     * @throws IOException
     * @throws FileNotFoundException
     */
    public void writeAppFile() throws IOException, FileNotFoundException {
        File file = new File(this.getPath())
        if (!file.getParentFile().exists()) {
            FileUtils.forceMkdir(file.getParentFile())
        }

        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)))
        pw.write(this.getQuery())
        pw.close()
    }
}