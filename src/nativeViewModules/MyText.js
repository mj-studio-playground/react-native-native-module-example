import React, {useRef, useImperativeHandle, useCallback} from 'react';
import {
  requireNativeComponent,
  UIManager,
  findNodeHandle,
  Platform,
} from 'react-native';

const COMPONENT_NAME = Platform.OS === 'ios' ? 'MyTextView' : 'MyText';
const NativeComponent = requireNativeComponent(COMPONENT_NAME);
const NativeViewManager = UIManager[COMPONENT_NAME];

const PROP_TEXT = 'textProp';
const COMMAND_SET_TEXT = 'setText';
const EVENT_ON_TEXT_CHANGED = 'onTextChanged';

const MyText = ({text, style, onTextChanged}, ref) => {
  const nativeRef = useRef(null);

  const manipulateTextWithUIManager = useCallback((text) => {
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(nativeRef.current),
      NativeViewManager.Commands[COMMAND_SET_TEXT],
      [text],
    );
  }, []);

  useImperativeHandle(
    ref,
    () => ({
      setText: manipulateTextWithUIManager,
    }),
    [manipulateTextWithUIManager],
  );

  return (
    <NativeComponent
      ref={nativeRef}
      style={[{height: 200}, style]}
      {...{
        [PROP_TEXT]: text,
        [EVENT_ON_TEXT_CHANGED]: ({nativeEvent: {text}}) => onTextChanged(text),
      }}
    />
  );
};

export default React.forwardRef(MyText);
