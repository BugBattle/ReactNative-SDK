require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = "BBBugBattle"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.homepage     = "https://www.bugbattle.app"
  s.license      = { :type => 'Commercial', :file => 'LICENSE.md' }
  s.author       = { "BugBattle" => "hello@bugbattle.app" }
  s.platform     = :ios, "9.0"
  s.source       = { :git => "https://github.com/BugBattle/BugBattle-ReactNative-SDK.git", :tag => "#{s.version}" }

  s.source_files = "ios/**/*.{h,m}"
  s.requires_arc = true
  
  s.dependency "React"
  s.dependency "BugBattle"
end

