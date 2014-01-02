package com.jellyfish85.query.generator.helper

import org.apache.commons.io.FileUtils


class ResourceCopyHelper {

    public void copyLoginSql(String destPath) {
        File loginFile =
                new File(getClass().getResource("/com/jellyfish85/query/generator/sql/login.sql").getPath())

        FileUtils.copyFile(loginFile, new File(destPath))
    }

}
