package com.gatebuzz.rapidapi.rx.example.slack;

import com.gatebuzz.rapidapi.rx.ApiPackage;
import com.gatebuzz.rapidapi.rx.DefaultParameters;
import com.gatebuzz.rapidapi.rx.Named;
import com.gatebuzz.rapidapi.rx.Required;
import io.reactivex.Observable;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

@SuppressWarnings({"unused", "SameParameterValue"})
@ApiPackage("Slack")
@DefaultParameters("token")
public interface SlackApi {
    //region Auth / Logs
    Observable<Map<String, Object>> testAuth();

    Observable<Map<String, Object>> revokeAuth(
            @Named("testMode") Integer testMode
    );

    Observable<Map<String, Object>> getAccessToken(
            @Required @Named("clientId") String clientId,
            @Required @Named("clientSecret") String clientSecret,
            @Required @Named("code") String code,
            @Required @Named("redirectUri") String redirectUri
    );

    Observable<Map<String, Object>> getAccessLogs(
            @Named("count") Integer count,
            @Named("page") Integer page

    );
    //endregion

    //region Bots
    Observable<Map<String, Object>> getBotInfo(
            @Named("bot") String bot
    );
    //endregion

    //region Channels
    Observable<Map<String, Object>> getChannels(
            @Named("excludeArchived") Boolean excludeArchived
    );

    Observable<Map<String, Object>> createChannel(
            @Named("name") String name
    );

    Observable<Map<String, Object>> archiveChannel(
            @Required @Named("channel") String channel
    );

    Observable<Map<String, Object>> getChannelHistory(
            @Required @Named("channel") String channel,
            @Named("latest") Long latest,
            @Named("oldest") Long oldest,
            @Named("inclusive") Boolean inclusive,
            @Named("count") Integer count,
            @Named("unreads") Boolean unreads
    );

    Observable<Map<String, Object>> getChannelInfo(
            @Required @Named("channel") String channel
    );

    Observable<Map<String, Object>> inviteToChannel(
            @Required @Named("channel") String channel,
            @Required @Named("user") String user
    );

    Observable<Map<String, Object>> joinChannel(
            @Required @Named("name") String name
    );

    Observable<Map<String, Object>> kickFromChannel(
            @Required @Named("channel") String channel,
            @Required @Named("user") String user
    );

    Observable<Map<String, Object>> leaveChannel(
            @Required @Named("channel") String channel
    );

    Observable<Map<String, Object>> markChannel(
            @Required @Named("channel") String channel,
            @Required @Named("timestamp") long timestamp
    );

    Observable<Map<String, Object>> renameChannel(
            @Required @Named("channel") String channel,
            @Required @Named("name") String name
    );

    Observable<Map<String, Object>> setChannelPurpose(
            @Required @Named("channel") String channel,
            @Required @Named("purpose") String purpose
    );

    Observable<Map<String, Object>> setChannelTopic(
            @Required @Named("channel") String channel,
            @Required @Named("topic") String topic
    );

    Observable<Map<String, Object>> unarchiveChannel(
            @Required @Named("channel") String channel
    );
    //endregion

    //region Private Channels
    Observable<Map<String, Object>> getPrivateChannels(
            @Named("excludeArchived") Boolean excludeArchived
    );

    Observable<Map<String, Object>> createPrivateChannel(
            @Required @Named("name") String name
    );

    Observable<Map<String, Object>> archivePrivateChannel(
            @Required @Named("channel") String channel
    );

    Observable<Map<String, Object>> getPrivateChannelHistory(
            @Required @Named("channel") String channel,
            @Named("latest") Long latest,
            @Named("oldest") Long oldest,
            @Named("inclusive") Boolean inclusive,
            @Named("count") Integer count,
            @Named("unreads") Boolean unreads
    );

    Observable<Map<String, Object>> getPrivateChannelInfo(
            @Required @Named("channel") String channel
    );

    Observable<Map<String, Object>> inviteToPrivateChannel(
            @Required @Named("channel") String channel,
            @Required @Named("user") String user
    );

    Observable<Map<String, Object>> kickFromPrivateChannel(
            @Required @Named("channel") String channel,
            @Required @Named("user") String user
    );

    Observable<Map<String, Object>> leavePrivateChannel(
            @Required @Named("channel") String channel
    );

    Observable<Map<String, Object>> markPrivateChannel(
            @Required @Named("channel") String channel,
            @Required @Named("timestamp") long timestamp
    );

    Observable<Map<String, Object>> renamePrivateChannel(
            @Required @Named("channel") String channel,
            @Required @Named("name") String name
    );

    Observable<Map<String, Object>> setPrivateChannelPurpose(
            @Required @Named("channel") String channel,
            @Required @Named("purpose") String purpose
    );

    Observable<Map<String, Object>> setPrivateChannelTopic(
            @Required @Named("channel") String channel,
            @Required @Named("topic") String topic
    );

    Observable<Map<String, Object>> unarchivePrivateChannel(
            @Required @Named("channel") String channel
    );

    Observable<Map<String, Object>> openPrivateChannel(
            @Required @Named("channel") String channel
    );

    Observable<Map<String, Object>> closePrivateChannel(
            @Required @Named("channel") String channel
    );
    //endregion

    //region Direct Messages
    Observable<Map<String, Object>> closeDirectMessageChannel(
            @Required @Named("channel") String channel
    );

    Observable<Map<String, Object>> getDirectMessageChannelHistory(
            @Required @Named("channel") String channel,
            @Named("latest") Long latest,
            @Named("oldest") Long oldest,
            @Named("inclusive") Boolean inclusive,
            @Named("count") Integer count,
            @Named("unreads") Boolean unreads
    );

    Observable<Map<String, Object>> getUserImChannels();

    Observable<Map<String, Object>> markDirectMessageChannel(
            @Required @Named("channel") String channel,
            @Required @Named("timestamp") long timestamp
    );

    Observable<Map<String, Object>> openDirectMessageChannel(
            @Required @Named("user") String user,
            @Named("returnIm") Boolean returnIm
    );

    Observable<Map<String, Object>> closeMultipartyDirectMessageChannel(
            @Required @Named("channel") String channel
    );

    Observable<Map<String, Object>> getMultipartyDirectMessageChannelHistory(
            @Required @Named("channel") String channel,
            @Named("latest") Long latest,
            @Named("oldest") Long oldest,
            @Named("inclusive") Boolean inclusive,
            @Named("count") Integer count,
            @Named("unreads") Boolean unreads
    );

    Observable<Map<String, Object>> getMultipartyDirectMessageChannels();

    Observable<Map<String, Object>> markMultipartyDirectMessageChannel(
            @Required @Named("channel") String channel,
            @Required @Named("timestamp") long timestamp
    );

    Observable<Map<String, Object>> openMultipartyDirectMessageChannel(
            @Required @Named("users") String users
    );
    //endregion

    //region Chat
    Observable<Map<String, Object>> deleteChat(
            @Required @Named("channel") String channel,
            @Required @Named("timestamp") long timestamp,
            @Named("asUser") Boolean asUser
    );

    Observable<Map<String, Object>> sendMeMessage(
            @Required @Named("channel") String channel,
            @Required @Named("text") String text
    );

    Observable<Map<String, Object>> postMessage(
            @Required @Named("channel") String channel,
            @Required @Named("text") String text,
            @Named("parse") String parse,
            @Named("linkNames") String linkNames,
            @Named("attachments") String attachments,
            @Named("unfurlLinks") Boolean unfurlLinks,
            @Named("unfurlMedia") Boolean unfurlMedia,
            @Named("username") String username,
            @Named("asUser") Boolean asUser,
            @Named("iconUrl") String iconUrl,
            @Named("iconEmoji") String iconEmoji
    );

    Observable<Map<String, Object>> updateMessage(
            @Named("channel") String channel,
            @Named("timestamp") long timestamp,
            @Named("text") String text,
            @Named("parse") String parse,
            @Named("linkNames") String linkNames,
            @Named("attachments") String attachments,
            @Named("asUser") Boolean asUser
    );

    Observable<Map<String, Object>> startRealTimeMessaging(
            @Named("simpleLatest") String simpleLatest,
            @Named("noUnreads") String noUnreads,
            @Named("mpimAware") String mpimAware
    );
    //endregion

    //region Status
    Observable<Map<String, Object>> endDoNotDisturb();

    Observable<Map<String, Object>> endSnooze();

    Observable<Map<String, Object>> getDoNotDisturbInfo(
            @Required @Named("user") String user
    );

    Observable<Map<String, Object>> setSnooze(
            @Required @Named("numMinutes") int minutes
    );

    Observable<Map<String, Object>> getTeamUserDoNotDisturbInfo(
            @Required @Named("users") String users
    );
    //endregion

    // region Emoji
    Observable<Map<String, Object>> getTeamCustomEmoji();
    // endregion

    //region Files
    Observable<Map<String, Object>> addFileComment(
            @Required @Named("file") String file,
            @Required @Named("comment") String comment,
            @Named("channel") String channel
    );

    Observable<Map<String, Object>> deleteFileComment(
            @Required @Named("file") String file,
            @Required @Named("id") String commentId
    );

    Observable<Map<String, Object>> updateFileComment(
            @Required @Named("file") String file,
            @Required @Named("id") String commentId,
            @Required @Named("comment") String comment
    );

    Observable<Map<String, Object>> deleteFile(
            @Required @Named("file") String file
    );

    Observable<Map<String, Object>> uploadFile(
            @Required @Named("file") InputStream file,
            @Named("filetype") String fileType,
            @Named("filename") String filename,
            @Named("title") String title,
            @Named("initialComment") String initialComment,
            @Named("channels") String channels
    );


    Observable<Map<String, Object>> getFileInfo(
            @Required @Named("file") String file,
            @Named("count") Integer count,
            @Named("page") Integer page
    );

    // TODO
    Observable<Map<String, Object>> getTeamFiles(
            @Named("user") String user,
            @Named("channel") String channel,
            @Named("timestampFrom") Long timestampFrom,
            @Named("timestampTo") Long timestampTo,
            @Named("types") String types,
            @Named("count") Integer count,
            @Named("page") Integer page
    );

    Observable<Map<String, Object>> disableFileSharing(
            @Required @Named("file") String file
    );

    Observable<Map<String, Object>> enableFileSharing(
            @Required @Named("file") String file
    );
    //endregion

    //region Pinned Items
    Observable<Map<String, Object>> pinItemToChannel(
            @Required @Named("channel") String channel,
            @Named("file") String file,
            @Named("fileComment") String fileComment,
            @Named("timestamp") Long timestamp
    );

    Observable<Map<String, Object>> getChannelPinnedItems(
            @Required @Named("channel") String channel
    );

    Observable<Map<String, Object>> unpinItemFromChannel(
            @Named("channel") String channel,
            @Named("file") String file,
            @Named("fileComment") String fileComment,
            @Named("timestamp") Long timestamp
    );
    //endregion

    //region Reactions
    Observable<Map<String, Object>> addReaction(
            @Required @Named("name") String name,
            @Named("channel") String channel,
            @Named("file") String file,
            @Named("fileComment") String fileComment,
            @Named("timestamp") Long timestamp
    );

    Observable<Map<String, Object>> getReaction(
            @Named("channel") String channel,
            @Named("file") String file,
            @Named("fileComment") String fileComment,
            @Named("timestamp") Long timestamp,
            @Named("full") Boolean full
    );

    Observable<Map<String, Object>> getReactions(
            @Named("user") String user,
            @Named("full") Boolean full,
            @Named("count") Integer count,
            @Named("page") Integer page
    );

    Observable<Map<String, Object>> removeReaction(
            @Required @Named("name") String name,
            @Named("channel") String channel,
            @Named("file") String file,
            @Named("fileComment") String fileComment,
            @Named("timestamp") Long timestamp
    );
    //endregion

    //region Search
    Observable<Map<String, Object>> searchItem(
            @Required @Named("query") String query,
            @Named("sort") String sort,
            @Named("sortDir") String sortDir,
            @Named("highlight") Integer highlight,
            @Named("count") Integer count,
            @Named("page") Integer page
    );
    //endregion

    //region Stars
    Observable<Map<String, Object>> addStar(
            @Named("channel") String channel,
            @Named("file") String file,
            @Named("fileComment") String fileComment,
            @Named("timestamp") Long timestamp
    );

    Observable<Map<String, Object>> getStars(
            @Named("count") Integer count,
            @Named("page") Integer page
    );

    Observable<Map<String, Object>> removeStar(
            @Named("channel") String channel,
            @Named("file") String file,
            @Named("fileComment") String fileComment,
            @Named("timestamp") Long timestamp
    );
    //endregion

    //region Team
    Observable<Map<String, Object>> getTeamInfo();

    Observable<Map<String, Object>> getTeamProfile(
            @Named("visibility") String visibility
    );
    //endregion

    //region User Groups
    Observable<Map<String, Object>> createUserGroup(
            @Required @Named("name") String name,
            @Named("handle") String handle,
            @Named("description") String description,
            @Named("channels") String channels,
            @Named("includeCount") String includeCount
    );

    Observable<Map<String, Object>> disableUserGroup(
            @Required @Named("usergroup") String usergroup,
            @Named("includeCount") String includeCount
    );

    Observable<Map<String, Object>> enableUserGroup(
            @Required @Named("usergroup") String usergroup,
            @Named("includeCount") String includeCount
    );

    Observable<Map<String, Object>> getUserGroups(
            @Named("includeDisabled") String includeDisabled,
            @Named("includeCount") String includeCount,
            @Named("includeUsers") String includeUsers
    );

    Observable<Map<String, Object>> updateUserGroup(
            @Named("usergroup") String usergroup,
            @Named("name") String name,
            @Named("handle") String handle,
            @Named("description") String description,
            @Named("channels") String channels,
            @Named("includeCount") String includeCount
    );

    Observable<Map<String, Object>> getUserGroupUsers(
            @Required @Named("usergroup") String usergroup,
            @Named("includeDisabled") String includeDisabled
    );

    Observable<Map<String, Object>> updateUserGroupUsers(
            @Required @Named("usergroup") String usergroup,
            @Named("users") String users,
            @Named("includeCount") String includeCount
    );
    //endregion

    //region User / Profile
    Observable<Map<String, Object>> getUserProfile(
            @Required @Named("user") String user,
            @Named("includeLabels") String includeLabels
    );

    Observable<Map<String, Object>> setUserProfile(
            @Required @Named("user") String user,
            @Named("profile") String profile,
            @Named("name") String name,
            @Named("value") String value
    );

    Observable<Map<String, Object>> deleteUserProfilePhoto();

    Observable<Map<String, Object>> getUserPresence(
            @Required @Named("user") String user
    );

    Observable<Map<String, Object>> getUserIdentity();

    Observable<Map<String, Object>> getUser(
            @Required @Named("user") String user
    );

    Observable<Map<String, Object>> getTeamUsers(
            @Named("presence") String presence
    );

    Observable<Map<String, Object>> setUserActive();

    Observable<Map<String, Object>> setPhoto(
            @Named("user") File image,
            @Named("cropX") Integer topLeftX,
            @Named("cropY") Integer topLeftY,
            @Named("cropW") Integer cropWidth
    );

    Observable<Map<String, Object>> setUserPresence(
            @Named("presence") String presence
    );
    //endregion

    //region Reminders
    Observable<Map<String, Object>> markReminderComplete(
            @Named("reminder") String reminder
    );

    Observable<Map<String, Object>> createReminder(
            @Required @Named("text") String text,
            @Required @Named("time") String time,
            @Named("user") String user
    );

    Observable<Map<String, Object>> deleteReminder(
            @Required @Named("reminder") String reminder
    );

    Observable<Map<String, Object>> getSingleReminder(
            @Required @Named("reminder") String reminder
    );

    Observable<Map<String, Object>> getReminders();
    //endregion
}
