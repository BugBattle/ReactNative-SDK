require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = "react-native-bugbattle"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.homepage     = "https://www.bugbattle.io"
  s.author       = { "BugBattle" => "hello@bugbattle.io" }

  s.license      = "Commercial"
  s.license    = { :type => "Commercial", :file => "LICENSE" }
  s.authors      = { "BugBattle GmbH" => "hello@bugbattle.io" }
  s.platforms    = { :ios => "9.0" }
  s.source       = { :git => "https://github.com/BugBattle/ReactNative-SDK.git", :tag => "#{s.version}" }

  s.source_files = "ios/**/*.{h,c,m,swift}"
  s.requires_arc = true

  s.dependency "React"
  s.dependency "BugBattle"
end

