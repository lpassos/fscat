package gsd.fscat

import java.io.File
import java.io.FileWriter
import gsd.fscat.c.CProjectInfo

/**
 * Created with IntelliJ IDEA.
 * User: Leonardo Passos
 * Date: 20/08/14
 * Time: 11:57 AM
 * To change this template use File | Settings | File Templates.
 */
object Main {

  private def writeSeq[T](output:String, header:String, entry2String: T => String, entries:Seq[T]) {
    val out = new FileWriter(output)
    out.write(header + "\n")
    entries.foreach(entry => out.write(entry2String(entry) + "\n"))
    out.close
  }

  private def saveAndReportCSV[T](outputDir:String,
                            filePrefix:String,
                            metric:String,
                            header:String,
                            entry2String: T => String,
                            entries:Seq[T]) = {

    val out = outputDir + File.separator + filePrefix + "_" + metric + ".csv"
    writeSeq(out, header, entry2String, entries)
    println("Metric " + metric + " saved in " + out)
  }


  def main(args:Array[String]) {

    if (args.length != 3) {
      println("Usage: fscat <project-src> <output-dir> <file-output-prefix>")
      System.exit(1)
    }

    val projectInfo = new CProjectInfo(new File(args(0)))
    println("Results\n\n")
    println("   * number of lines of code ...................... " + projectInfo.loc + "\n")
    println("   * number of feature constants .................. " + projectInfo.ftConstants.size + "\n")


    saveAndReportCSV(args(1), args(2), "sd", "feature;sd",
      {e:((String, Int)) => e._1 + ";" + e._2}, projectInfo.sd.toSeq
    )

    saveAndReportCSV(args(1), args(2), "td", "guard;line;td",
      {e:((String, Int, Int)) => e._1 + ";" + e._2 + ";" + e._3}, projectInfo.td.toSeq
    )

    saveAndReportCSV(args(1), args(2), "bmnd", "branch;line;td",
      {e:((String, Int, Int)) => e._1 + ";" + e._2 + ";" + e._3}, projectInfo.bmnd.toSeq
    )
  }
}
