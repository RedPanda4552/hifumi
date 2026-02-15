package net.pcsx2.hifumi.async;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReference;
import net.dv8tion.jda.api.entities.MessageReference.MessageReferenceType;
import net.dv8tion.jda.api.entities.User;
import net.pcsx2.hifumi.util.Messaging;

public class MaliciousForwardRunnable implements Runnable
{
	private final Message message;
	
	public MaliciousForwardRunnable(Message message)
	{
		this.message = message;
	}

	@Override
	public void run()
	{
		MessageReference ref = this.message.getMessageReference();
		
		if (ref != null && ref.getType() == MessageReferenceType.FORWARD)
		{
			Message forwardedMessage = ref.resolve().complete();
			
			if (!forwardedMessage.getComponents().isEmpty())
			{
				this.message.delete().queue();
				User user = this.message.getAuthor();
				Messaging.sendPrivateMessage(user, "Forwarding messages which contain buttons or other interactive items is not allowed for security reasons.");
			}
		}
		
	}
}
