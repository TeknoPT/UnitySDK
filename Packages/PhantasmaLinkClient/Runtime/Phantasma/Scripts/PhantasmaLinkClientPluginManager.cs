using System;
using System.Collections;
using System.Collections.Generic;
using System.Threading.Tasks;
using TMPro;
using UnityEngine;

public class PhantasmaLinkClientPluginManager : MonoBehaviour
{
    public static PhantasmaLinkClientPluginManager Instance { get; private set; }
    [SerializeField] private const string PluginName = "com.phantasma.phantasmalinkclient.PhantasmaLinkClientClass";
    [SerializeField] private TMP_Text DebugOutput;

    private AndroidJavaClass UnityClass;
    private AndroidJavaObject UnityActivity;
    private AndroidJavaObject PluginInstance;
    private AndroidJavaObject PluginClass;

    private void Awake() => Instance = this;

    void Start()
    {
#if UNITY_ANDROID || UNITY_EDITOR
        InitializePlugin(PluginName);
#endif
    }

    private void InitializePlugin(string pluginName)
    {
#if UNITY_ANDROID
        PluginClass = new AndroidJavaClass(pluginName);
        UnityClass = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
        UnityActivity = UnityClass.GetStatic<AndroidJavaObject>("currentActivity");
        PluginInstance  = PluginClass.CallStatic<AndroidJavaObject>("getInstance");
        if (PluginInstance == null)
        {
            Debug.LogError("Error Loading Plugin..");
        }
        
        PluginInstance.CallStatic("ReceiveActivity", UnityActivity);
        PhantasmaLinkClient.Instance.Enable();
#endif
    }

    public void OpenWallet()
    {
        #if UNITY_ANDROID
        PluginInstance.Call("OpenWallet");
        #endif
    } 


    public async Task SendTransaction(string tx)
    {
        #if UNITY_ANDROID
        //var result = PluginInstance.Call<string>("SendMyCommand", tx);
        OnSendTransaction(tx);
        await Task.Delay(0);
        #endif
    }

    public void Example()
    {
        #if UNITY_ANDROID
        PhantasmaLinkClient.Instance.Login();
        #endif
    }

    #region Send Transaction

    private static bool IsSending;
    // Interface implementation
    class SendTransactionCallback: AndroidJavaProxy
    {
        private System.Action<int> sendHandler;
        public SendTransactionCallback(System.Action<int> sendHandlerIn) : base (PluginName + "$SendTransactionCallback")
        {
            sendHandler = sendHandlerIn;
        }
        
        public void onShareComplete(int result)
        {
            Debug.Log("ShareComplete:" + result);
            IsSending = false;
            if (sendHandler != null)
                sendHandler(result);
        }
    }
    
    public void OnSendTransaction(string transaction)
    {
        SendTransaction(transaction, (int result) => {
            Debug.Log("Send complete with: " + result);
        });
    }

    public void SendTransaction(string transaction, System.Action<int> sendComplete)
    {
        if (IsSending)
        {
            Debug.LogError("Already sending transaction");
            return;
        }
        
        IsSending = true;
        StartCoroutine(waitForEndOfFrame(transaction, sendComplete));
    }

    IEnumerator waitForEndOfFrame(string transaction, System.Action<int> sendComplete)
    {
        yield return new WaitForEndOfFrame();
        Debug.Log("transaction:"+transaction);
        if (Application.platform == RuntimePlatform.Android)
        {
            PluginInstance.Call("sendTransaction", new object[] { transaction, new SendTransactionCallback(sendComplete) });
        }
    }
    
    #endregion
}
