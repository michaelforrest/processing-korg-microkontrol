package microkontrol.controls;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class KorgControl {
	protected ArrayList<CallBack> callbacks = new ArrayList<CallBack>();


	protected class CallBack{
		private final Object object;
		private final Method method;

		public CallBack(Object object, Method method){
			this.object = object;
			this.method = method;
		}
		public Object getObject() {
			return object;
		}
		public Method getMethod() {
			return method;
		}
		public void invoke() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
			method.invoke(object);

		}
	}
}
