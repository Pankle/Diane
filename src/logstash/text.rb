def register(params)
         @message = params["message"]             # 通过params获取script_params传参
end

def filter(event)
	val2 = "This is variable two"
        puts val2
	msg = event.get(@message)
	puts msg
	if(msg =~ /id_card:(.*)/)                                      # 获取日志中的关键字certNo
		cerNoExp = /(?<=id_card:)(\w{6})(\w{8})(\w{4})/
		cerNoExp =~ msg
		cerNoInFor = $2
		msg.gsub!(cerNoInFor, "******")
	end
	if(msg =~ /mobileNo:(.*)/)                                  # 获取日志中的关键字mobileNo
		puts msg
                mobileNoExp = /(?<=mobileNo:)(\w{3})(\w{4})(\w{4})/
                mobileNoExp =~ msg
                mobileNoInFor = $2
                msg.gsub!(mobileNoInFor, "****")
        end
	#event.set("msg", msg)
	return [event]	
end
