require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = "react-native-bugbattle-sdk"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.description  = <<-DESC
                  react-native-bugbattle-sdk
                   DESC
  s.homepage     = "https://www.bugbattle.io"
  s.license      = "MIT"
  s.authors      = { "BugBattle" => "hello@bugbattle.io" }
  s.platforms    = { :ios => "9.0" }
  s.source       = { :git => "https://github.com/BugBattle/ReactNative-SDK.git", :tag => "#{s.version}" }

  s.source_files = "ios/**/*.{h,c,m,swift}"
  s.requires_arc = true

  s.dependency "React"
  s.dependency "BugBattle"
end

