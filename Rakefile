require 'rexml/document'
include REXML

desc "update version and copy to server"
task :release do
  Dir.chdir("resources") do
    build = File.read("build.xml")
    xml = Document.new(build)
    versionNumber = xml.elements["project/property[@name='versionNumber']"]
    v = versionNumber.attributes["value"]
    nummers = v.split(".")
    minorVersion = nummers[2]
    minorVersion = minorVersion.to_i + 1
    versionNumber.attributes["value"] = nummers[0..1].join(".") + "." + minorVersion.to_s
    File.open("build.xml", "w") do |file|
      file << xml.to_s
    end
    puts `ant`
  end
  `scp -r distribution/web/* root@grimaceworks.com:/srv/processing/microkontrol/`
end