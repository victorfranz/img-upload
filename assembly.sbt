import sbtassembly.Plugin.AssemblyKeys._

assemblySettings

jarName in assembly := { s"${name.value}-${version.value}.jar" }

mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
  {
    case PathList("org", "joda", xs @ _*) => MergeStrategy.last
    case x => old(x)
  }
}