def register(params)
         @message = params["message"]             # 通过params获取script_params传参
end
def filter(event)
        info=event.get("message")
        #puts info
	infos=info.split(",")
	if infos.length == 6
		puts infos[0]
		s=''
		s<<"id:"  << infos[0]  << ",userid:" << infos[1] <<",submit_date:" <<infos[2] << ",mobileNo:" << infos[3] << ",address:" << infos[4] << ",id_card:" << infos[5]
		event.set("message",s)
        puts s
	end
        #event.set("msg", msg)
        
	return [event]
end
